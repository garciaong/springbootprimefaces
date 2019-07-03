package com.springboot.primefaces.csrf;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomCsrfFilter extends OncePerRequestFilter{

	private final CsrfTokenRepository csrfTokenRepo;
	private final Log logger = LogFactory.getLog(getClass());
	private RequestMatcher requireCsrfProtectionMatcher = new DefaultRequiresCsrfMatcher();
	private AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();
	
	public CustomCsrfFilter() {
		this.csrfTokenRepo = new HttpSessionCsrfTokenRepository();
	}
	
	/**
	 * Copycat...modify spring security CSRF filter to suits personal taste
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("Invoking doFilterInternal() of CustomCsrfFilter class");
		CsrfToken csrfToken = this.csrfTokenRepo.loadToken(request);
		final boolean missingToken = csrfToken == null;
		log.info("Already has CSRF token? : "+(!missingToken));
		if (missingToken) {
			csrfToken = this.csrfTokenRepo.generateToken(request);
			this.csrfTokenRepo.saveToken(csrfToken, request, response);
			log.info("New CSRF token generated : "+csrfToken.getToken());
		}
		request.setAttribute(CsrfToken.class.getName(), csrfToken);
		request.setAttribute(csrfToken.getParameterName(), csrfToken);
		log.info("URI : "+request.getRequestURI());
		if (!this.requireCsrfProtectionMatcher.matches(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		/**
		 * Priority will validate header csrf first (aka X-CSRF-TOKEN)
		 * If header token not found then get the form parameter csrf (aka _csrf)
		 */
		String actualToken = request.getHeader(csrfToken.getHeaderName());
		if (actualToken == null) {log.info("Csrf param : "+csrfToken.getParameterName());
			actualToken = request.getParameter(csrfToken.getParameterName());
		}
		if (!csrfToken.getToken().equals(actualToken)) {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Invalid CSRF token found for "
						+ UrlUtils.buildFullRequestUrl(request));
			}
			if (missingToken) {
				this.accessDeniedHandler.handle(request, response,
						new MissingCsrfTokenException(actualToken));
			}
			else {
				this.accessDeniedHandler.handle(request, response,
						new InvalidCsrfTokenException(csrfToken, actualToken));
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
}
