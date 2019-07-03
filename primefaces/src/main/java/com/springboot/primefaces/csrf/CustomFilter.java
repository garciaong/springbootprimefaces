package com.springboot.primefaces.csrf;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.springboot.primefaces.TokenGenerator;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomFilter extends OncePerRequestFilter {
	
	private static final String [] FILTER_URL = {"posted.xhtml","testcsrf"}; 
	private static final String CSRF_TOKEN = "custom_csrf";
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getRequestURL().toString().contains(".xhtml")) {
			if (Arrays.asList("POST", "PUT", "DELETE").contains(request.getMethod())) {
				log.info("Token In Session:" + request.getSession().getAttribute(CSRF_TOKEN));
				log.info("Token In Request:" + request.getParameter(CSRF_TOKEN));
				log.info("URL:" + request.getRequestURL().toString());
				if(Arrays.stream(FILTER_URL).parallel().anyMatch(request.getRequestURL().toString()::contains)) {
					if (request.getSession().getAttribute(CSRF_TOKEN)==null || 
							!request.getSession().getAttribute(CSRF_TOKEN).equals(request.getParameter(CSRF_TOKEN))) {
						response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid CSRF token!");
					}
				}
			} else {
				if(request.getSession() != null && request.getSession().getAttribute(CSRF_TOKEN) == null) {
					request.getSession().setAttribute(CSRF_TOKEN, TokenGenerator.generateToken());
					log.info("Token Generated:" + request.getSession().getAttribute(CSRF_TOKEN));
				}
			}
		}

		filterChain.doFilter(request, response);
	}
}