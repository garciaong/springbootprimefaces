package com.springboot.primefaces.impl;

import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

@ManagedBean(name = "userView")
@ViewScoped
@Data
@Log4j2
public class UserView {

	private String result;
	private User user = new User();

	public String save() {
		return "/user.xhtml";
	}

	public void validate() {
		log.info("validating...");
		if (user.getName()!=null && user.getName().length() > 0) {
			if (!Pattern.matches("[A-Za-z\\s]*", user.getName())) {
				this.result = "Name must be alphabets only";
			} else {
				this.result = "";
			}
		}
	}
	
	public String getResult() {
		return result;
	}

	public User getUser() {
		return user;
	}

}