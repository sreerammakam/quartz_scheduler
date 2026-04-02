package com.lca.eps.common.apic;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.lca.eps.common.EPSRuntimeException;
import com.lca.eps.common.persistence.common.UnitOfWork;
import com.lca.eps.common.persistence.dao.PropertyValueDao;
import com.lca.eps.common.persistence.entity.PropertyValueEntity;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class APICAccessTokenFetchService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	private final Object refreshLock = new Object();

	private final SessionFactory sessionFactory;
	private final PropertyValueDao propertyValueDao;

	@Autowired
	public APICAccessTokenFetchService(SessionFactory sessionFactory,
	                                   PropertyValueDao propertyValueDao) {
		this.sessionFactory = sessionFactory;
		this.propertyValueDao = propertyValueDao;
	}

	public String getOrRefreshAccessToken() {
		try {
			var currentAccessTokenDetails = getCurrentAccessTokenDetails();
			if (Objects.nonNull(currentAccessTokenDetails) &&
			    !isTokenExpired(currentAccessTokenDetails.getExpiryTime())) {
				return currentAccessTokenDetails.getAccessToken();
			}

			synchronized (refreshLock) {
				currentAccessTokenDetails = getCurrentAccessTokenDetails();
				if (Objects.nonNull(currentAccessTokenDetails) &&
				    !isTokenExpired(currentAccessTokenDetails.getExpiryTime())) {
					return currentAccessTokenDetails.getAccessToken();
				}
				var newAccessTokenDetails = generateAndSaveToken();
				return newAccessTokenDetails.getAccessToken();
			}
		} catch (Exception e) {
			var errorMessage = "Error during APIC token refresh: %s".formatted(Throwables.getStackTraceAsString(e));
			logger.error(errorMessage);
			throw new EPSRuntimeException(errorMessage);
		}
	}

	public String getApicClientId() {
		var unitOfWork = new UnitOfWork(sessionFactory);
		try {
			var propertyValue = propertyValueDao.findByPropertyName(Apic.APIC_CLIENT_ID, unitOfWork);
			return propertyValue.getPropertyValue();
		} finally {
			unitOfWork.endSession();
		}
	}


	private AccessTokenDetails getCurrentAccessTokenDetails() throws IOException {
		var uow = new UnitOfWork(sessionFactory);
		try {
			var accessTokenDetailsPropertyData = propertyValueDao.findByPropertyName(Apic.APIC_ACCESS_TOKEN_DETAILS, uow);
			if (Objects.isNull(accessTokenDetailsPropertyData)) {
				return null;
			}
			var accessTokenDetailsPropertyValue = accessTokenDetailsPropertyData.getPropertyValue();
			if (StringUtils.isNotBlank(accessTokenDetailsPropertyValue)) {
				var objectMapper = new ObjectMapper();
				return objectMapper.readValue(accessTokenDetailsPropertyValue, AccessTokenDetails.class);
			}
		} finally {
			uow.endSession();
		}
		return null;
	}

	private boolean isTokenExpired(Date expiryTime) {
		Date fiveMinutesFromCurrentTime = DateUtils.addMinutes(new Date(), 5);
		return Objects.isNull(expiryTime) || expiryTime.before(fiveMinutesFromCurrentTime);
	}

	private AccessTokenDetails generateAndSaveToken() throws IOException {
		var propertyMap = getAccessTokenConfig();
		var accessTokenResponse = generateAccessToken(propertyMap);

		var accessTokenDetails = new AccessTokenDetails();
		accessTokenDetails.setAccessToken(accessTokenResponse.getAccessToken());

		int expiresIn = Integer.parseInt(propertyMap.get(Apic.APIC_ACCESS_TOKEN_EXPIRES_IN).getPropertyValue());
		accessTokenDetails.setExpiryTime(calculateTokenExpiration(expiresIn));
		accessTokenDetails.setExpiresIn(accessTokenResponse.getExpiresIn());
		accessTokenDetails.setTokenType(accessTokenResponse.getTokenType());

		saveAccessToken(accessTokenDetails);
		return accessTokenDetails;
	}

	private Map<String, PropertyValueEntity> getAccessTokenConfig() {
		var uow = new UnitOfWork(sessionFactory);
		try {
			var propertiesNames = Arrays.asList(
					Apic.APIC_CLIENT_ID,
					Apic.APIC_CLIENT_SECRET,
					Apic.APIC_GRANT_TYPE,
					Apic.APIC_SCOPE,
					Apic.APIC_ACCESS_TOKEN_URL,
					Apic.APIC_ACCESS_TOKEN_EXPIRES_IN
			);
			var config = propertyValueDao.findByPropertyName(propertiesNames, uow);
			var propertyMap = new HashMap<String, PropertyValueEntity>();
			for (var propertyValueEntity : config) {
				propertyMap.put(propertyValueEntity.getPropertyName(), propertyValueEntity);
			}
			return propertyMap;
		} finally {
			uow.endSession();
		}
	}

	private AccessTokenResponse generateAccessToken(Map<String, PropertyValueEntity> propertyMap) throws IOException {
		try (var client = new JerseyClientBuilder().build()) {
			var apicAccessTokenUrl = propertyMap.get(Apic.APIC_ACCESS_TOKEN_URL).getPropertyValue();
			var webTarget = client.target(apicAccessTokenUrl);
			var formEntity = Entity.form(getFormParams(propertyMap));
			var response = webTarget.request().post(formEntity);

			var responseAsString = response.readEntity(String.class);

			if (isValidResponse(response, responseAsString)) {
				var accessTokenResponse = parseResponse(responseAsString);
				if (StringUtils.isNotEmpty(accessTokenResponse.getAccessToken())) {
					return accessTokenResponse;
				}
			}
			throw new EPSRuntimeException("Error during fetching of APIC token. Response code: %s Response: %s".formatted(response.getStatus(), responseAsString));
		}
	}

	private static Form getFormParams(Map<String, PropertyValueEntity> propertyMap) {
		var form = new Form();
		form.param("client_id", propertyMap.get(Apic.APIC_CLIENT_ID).getPropertyValue());
		form.param("client_secret", propertyMap.get(Apic.APIC_CLIENT_SECRET).getPropertyValue());
		form.param("grant_type", propertyMap.get(Apic.APIC_GRANT_TYPE).getPropertyValue());
		form.param("scope", propertyMap.get(Apic.APIC_SCOPE).getPropertyValue());
		return form;
	}

	private boolean isValidResponse(Response response, String responseAsString) {
		return response.getStatus() == 200 && StringUtils.isNotEmpty(responseAsString);
	}

	private AccessTokenResponse parseResponse(String responseAsString) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper.readValue(responseAsString, AccessTokenResponse.class);
	}

	private Date calculateTokenExpiration(int expiresIn) {
		return DateUtils.addSeconds(new Date(), expiresIn);
	}

	private void saveAccessToken(AccessTokenDetails accessTokenDetails) throws JsonProcessingException {
		var uow = new UnitOfWork(sessionFactory);
		try {
			var mapper = new ObjectMapper();
			var accessTokenDataAsString = mapper.writeValueAsString(accessTokenDetails);

			uow.beginTransaction();
			propertyValueDao.updatePropertyValueByName(Apic.APIC_ACCESS_TOKEN_DETAILS, accessTokenDataAsString, uow);
			uow.commit();
		} finally {
			uow.endSession();
		}
	}

	public static class Apic {
		public static final String APIC_CLIENT_ID = "APIC_CLIENT_ID";
		public static final String APIC_CLIENT_SECRET = "APIC_CLIENT_SECRET";
		public static final String APIC_GRANT_TYPE = "APIC_GRANT_TYPE";
		public static final String APIC_SCOPE = "APIC_SCOPE";
		public static final String APIC_ACCESS_TOKEN_DETAILS = "APIC_ACCESS_TOKEN_DETAILS";
		public static final String APIC_ACCESS_TOKEN_URL = "APIC_ACCESS_TOKEN_URL";
		public static final String APIC_ACCESS_TOKEN_EXPIRES_IN = "APIC_ACCESS_TOKEN_EXPIRES_IN";
	}
}

