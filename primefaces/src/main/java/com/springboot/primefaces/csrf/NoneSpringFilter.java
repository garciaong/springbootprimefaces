package com.springboot.primefaces.csrf;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.util.matcher.RequestMatcher;

import lombok.extern.log4j.Log4j2;

@Log4j2
@WebFilter(urlPatterns = {"/*"})
public class NoneSpringFilter implements Filter{

	private final CustomCsrfTokenRepository csrfTokenRepo;
	private RequestMatcher requireCsrfProtectionMatcher = new DefaultRequiresCsrfMatcher();
	private final boolean csrfTokenWithOriginValidation = true;
	private static final String REQUEST_ORIGIN = "origin";
	
	public NoneSpringFilter() {
		this.csrfTokenRepo = new CustomHttpSessionCsrfTokenRepository();
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		log.info("None Spring filter...");
		if(csrfTokenWithOriginValidation) {
			csrfValidationWithOrigin((HttpServletRequest)request, (HttpServletResponse)response, filterChain);
		} else {
			csrfStandardValidation((HttpServletRequest)request, (HttpServletResponse)response, filterChain);
		}
	}
	
	private void csrfValidationWithOrigin(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		log.info("Invoking csrfValidationWithClient()");
		CustomCsrfToken csrfToken = this.csrfTokenRepo.loadToken(request);
		final boolean missingToken = csrfToken == null;
		log.info("Already has CSRF token? : "+(!missingToken));
		if (missingToken) {
			csrfToken = this.csrfTokenRepo.generateToken(request);
			this.csrfTokenRepo.saveToken(csrfToken, request, response);
			log.info("New CSRF token generated : "+csrfToken.getToken());
		}
		request.setAttribute(CustomCsrfToken.class.getName(), csrfToken);
		request.setAttribute(csrfToken.getParameterName(), csrfToken);
		
		if (!this.requireCsrfProtectionMatcher.matches(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		Map<String,String> headers = getHeadersInfo(request);
		/**
		 * Priority will validate header csrf first (aka X-CSRF-TOKEN)
		 * If header token not found then get the form parameter csrf (aka _csrf)
		 */
		String actualToken = request.getHeader(csrfToken.getHeaderName());
		if (actualToken == null) {
			actualToken = request.getParameter(csrfToken.getParameterName());
		}
		
		if (missingToken) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "CSRF token not found!");
			return;
		}
		
		if (!csrfToken.getToken().equals(actualToken)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token!");
			return;
		}
		
		if (headers.get(REQUEST_ORIGIN)==null || "null".equalsIgnoreCase(headers.get(REQUEST_ORIGIN))) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid access from unauthorized origin!");
			return;
		}
		filterChain.doFilter(request, response);
	}
	
	private void csrfStandardValidation(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		log.info("Invoking csrfStandardValidation()");
		CustomCsrfToken csrfToken = this.csrfTokenRepo.loadToken(request);
		final boolean missingToken = csrfToken == null;
		log.info("Already has CSRF token? : "+(!missingToken));
		if (missingToken) {
			csrfToken = this.csrfTokenRepo.generateToken(request);
			this.csrfTokenRepo.saveToken(csrfToken, request, response);
			log.info("New CSRF token generated : "+csrfToken.getToken());
		}
		request.setAttribute(CustomCsrfToken.class.getName(), csrfToken);
		request.setAttribute(csrfToken.getParameterName(), csrfToken);
		
		if (!this.requireCsrfProtectionMatcher.matches(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		/**
		 * Priority will validate header csrf first (aka X-CSRF-TOKEN)
		 * If header token not found then get the form parameter csrf (aka _csrf)
		 */
		String actualToken = request.getHeader(csrfToken.getHeaderName());
		if (actualToken == null) {
			actualToken = request.getParameter(csrfToken.getParameterName());
		}
		
		if (!csrfToken.getToken().equals(actualToken)) {
			if (missingToken) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "CSRF token not found!");
			}
			else {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token!");
			}
			return;
		}
		filterChain.doFilter(request, response);
	}
	
	private static final class DefaultRequiresCsrfMatcher implements RequestMatcher {
		private final HashSet<String> allowedMethods = new HashSet<>(
				Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));
		
		@Override
		public boolean matches(HttpServletRequest request) {
			return !this.allowedMethods.contains(request.getMethod());
		}
	}
	
	private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }
	
}
