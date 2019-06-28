package com.springboot.primefaces.impl;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

@ManagedBean(name = "postView")
@ViewScoped
@Data
@Log4j2
public class PostView {

	private String param1;
	private String param2;
	
	@PostConstruct
	public void init() {
		log.info("Initializing post request...");
		ExternalContext extCtx = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest)extCtx.getRequest();
		param1 = request.getParameter("param1");
		param2 = request.getParameter("param2");
		String abc = request.getParameter("form1:abc");
		log.info("param1:"+param1);
		log.info("param2:"+param2);
		log.info("abc:"+abc);
	}
	
	public void initTest() {
		log.info("PostView bean initialized...");
	}
	
}
