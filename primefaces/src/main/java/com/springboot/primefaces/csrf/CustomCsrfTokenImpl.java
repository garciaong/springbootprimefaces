package com.springboot.primefaces.csrf;

import org.springframework.util.Assert;

@SuppressWarnings("serial")
public final class CustomCsrfTokenImpl implements CustomCsrfToken{

	private final String token;
	private final String parameterName;
	private final String headerName;
	
	/**
	 * All parameters are mandatory except for previousUri (only mandatory when token generation per request
	 */
	public CustomCsrfTokenImpl(String headerName, String parameterName, String token) {
		Assert.hasLength(headerName, "headerName cannot be null or empty");
		Assert.hasLength(parameterName, "parameterName cannot be null or empty");
		Assert.hasLength(token, "token cannot be null or empty");
		this.headerName = headerName;
		this.parameterName = parameterName;
		this.token = token;
	}
	
	@Override
	public String getHeaderName() {
		return this.headerName;
	}

	@Override
	public String getParameterName() {
		return this.parameterName;
	}

	@Override
	public String getToken() {
		return this.token;
	}
}
