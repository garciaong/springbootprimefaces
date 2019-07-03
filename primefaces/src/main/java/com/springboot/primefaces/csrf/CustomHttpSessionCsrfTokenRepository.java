package com.springboot.primefaces.csrf;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.Assert;

public class CustomHttpSessionCsrfTokenRepository implements CustomCsrfTokenRepository {
	private static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";

	private static final String DEFAULT_CSRF_HEADER_NAME = "X-CSRF-TOKEN";

	private static final String DEFAULT_CSRF_TOKEN_ATTR_NAME = CustomHttpSessionCsrfTokenRepository.class
			.getName().concat(".CSRF_TOKEN");

	private String parameterName = DEFAULT_CSRF_PARAMETER_NAME;

	private String headerName = DEFAULT_CSRF_HEADER_NAME;

	private String sessionAttributeName = DEFAULT_CSRF_TOKEN_ATTR_NAME;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.security.web.csrf.CsrfTokenRepository#saveToken(org.
	 * springframework .security.web.csrf.CsrfToken,
	 * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void saveToken(CustomCsrfToken token, HttpServletRequest request,
			HttpServletResponse response) {
		if (token == null) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.removeAttribute(this.sessionAttributeName);
			}
		}
		else {
			HttpSession session = request.getSession();
			session.setAttribute(this.sessionAttributeName, token);
		}
	}
	
	public CustomCsrfToken loadToken(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		return (CustomCsrfToken) session.getAttribute(this.sessionAttributeName);
	}
	
	public CustomCsrfToken generateToken(HttpServletRequest request) {
		return new CustomCsrfTokenImpl(this.headerName, this.parameterName,
				createNewToken());
	}

	public void setParameterName(String parameterName) {
		Assert.hasLength(parameterName, "parameterName cannot be null or empty");
		this.parameterName = parameterName;
	}

	public void setHeaderName(String headerName) {
		Assert.hasLength(headerName, "headerName cannot be null or empty");
		this.headerName = headerName;
	}
	
	public void setSessionAttributeName(String sessionAttributeName) {
		Assert.hasLength(sessionAttributeName,
				"sessionAttributename cannot be null or empty");
		this.sessionAttributeName = sessionAttributeName;
	}

	private String createNewToken() {
		return UUID.randomUUID().toString();
	}
}
