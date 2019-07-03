package com.springboot.primefaces.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CustomCsrfTokenRepository {
	
	CustomCsrfToken generateToken(HttpServletRequest request);
	
	void saveToken(CustomCsrfToken token, HttpServletRequest request,
			HttpServletResponse response);
	
	CustomCsrfToken loadToken(HttpServletRequest request);
}
