package com.springboot.primefaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class RequestInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		log.info("Entering interceptor");
		String paramName;
		while(request.getParameterNames().hasMoreElements()) {
			paramName = request.getParameterNames().nextElement();
			log.info("Request received : [param="+paramName+", value="+request.getParameter(paramName)+"]");
		}
		
		return true;
	}
}
