package com.lca.eps.common.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import java.util.UUID;

public class DistributedRequestIdFilter implements ClientRequestFilter {

	private static final String EPS_REQUEST_UUID = "eps-requestUUID";

	@Override
	public void filter(ClientRequestContext requestContext) {
		String requestUUID = ThreadContext.get(EPS_REQUEST_UUID);
		if (StringUtils.isBlank(requestUUID)) {
			requestUUID = UUID.randomUUID().toString();
		}
		ThreadContext.put(EPS_REQUEST_UUID, requestUUID);
		requestContext.getHeaders().add(EPS_REQUEST_UUID, requestUUID);
	}
}
