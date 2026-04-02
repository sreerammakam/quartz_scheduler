package com.lca.eps.common.apic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class AccessTokenResponse {

	private String accessToken;
	private String tokenType;
	private int expiresIn;

	@JsonProperty("accessToken")
	public String getAccessToken() {
		return accessToken;
	}

	@JsonProperty("access_token")
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@JsonProperty("tokenType")
	public String getTokenType() {
		return tokenType;
	}

	@JsonProperty("token_type")
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	@JsonProperty("expiresIn")
	public int getExpiresIn() {
		return expiresIn;
	}

	@JsonProperty("expires_in")
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	@Override
	public String toString() {
		return "AccessTokenResponse{" +
		       "accessToken='" + accessToken + '\'' +
		       ", tokenType='" + tokenType + '\'' +
		       ", expiresIn=" + expiresIn +
		       '}';
	}
}
