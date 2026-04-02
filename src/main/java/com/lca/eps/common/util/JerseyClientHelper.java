package com.lca.eps.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.lca.eps.common.EPSException;
import com.lca.eps.common.apic.APICAccessTokenFetchService;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

@Component
public class JerseyClientHelper {

	private final APICAccessTokenFetchService apicAccessTokenFetchService;
	private final Environment environment;

	@Autowired
	public JerseyClientHelper(APICAccessTokenFetchService apicAccessTokenFetchService,
	                          Environment environment) {
		this.apicAccessTokenFetchService = apicAccessTokenFetchService;
		this.environment = environment;
	}

	public Response doGETRequest(Client client, String targetUrl) throws IOException, EPSException {
		var apicClientId = apicAccessTokenFetchService.getApicClientId();
		var apicAccessTokenDetails = apicAccessTokenFetchService.getOrRefreshAccessToken();

		return client.target(targetUrl)
				.request()
				.header("x-ibm-client-id", apicClientId)
				.header("Origin", environment.getRequiredProperty("application.host"))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + apicAccessTokenDetails)
				.get();
	}

	public Response doGETRequest(Client client, String targetUrl, Map<String, Object> queryParams) throws IOException, EPSException {
		var apicClientId = apicAccessTokenFetchService.getApicClientId();
		var apicAccessTokenDetails = apicAccessTokenFetchService.getOrRefreshAccessToken();

		var webTarget = client.target(targetUrl);
		for (var queryParam : queryParams.entrySet()) {
			webTarget = webTarget.queryParam(queryParam.getKey(), queryParam.getValue());
		}
		return webTarget.request()
				.header("x-ibm-client-id", apicClientId)
				.header("Origin", environment.getRequiredProperty("application.host"))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + apicAccessTokenDetails)
				.get();
	}

	public <T> Response doPOSTRequest(Client client, String targetUrl, T entity) {
		var apicClientId = apicAccessTokenFetchService.getApicClientId();
		var apicAccessTokenDetails = apicAccessTokenFetchService.getOrRefreshAccessToken();

		return client.target(targetUrl)
				.request()
				.header("x-ibm-client-id", apicClientId)
				.header("Origin", environment.getRequiredProperty("application.host"))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + apicAccessTokenDetails)
				.post(Entity.entity(entity, MediaType.APPLICATION_JSON));
	}
}
