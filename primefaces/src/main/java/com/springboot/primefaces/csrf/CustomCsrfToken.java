package com.springboot.primefaces.csrf;

import java.io.Serializable;

public interface CustomCsrfToken extends Serializable {

	/**
	 * Gets the HTTP header that the CSRF is populated on the response and can be placed
	 * on requests instead of the parameter. Cannot be null.
	 *
	 * @return the HTTP header that the CSRF is populated on the response and can be
	 * placed on requests instead of the parameter
	 */
	String getHeaderName();

	/**
	 * Gets the HTTP parameter name that should contain the token. Cannot be null.
	 * @return the HTTP parameter name that should contain the token.
	 */
	String getParameterName();

	/**
	 * Gets the token value. Cannot be null.
	 * @return the token value
	 */
	String getToken();
}