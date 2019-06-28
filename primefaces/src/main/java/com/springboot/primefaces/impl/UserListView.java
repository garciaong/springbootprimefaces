package com.springboot.primefaces.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.Data;

@ManagedBean(name = "userlistView")
@ViewScoped
@Data
public class UserListView {

	private List<User> users = new ArrayList<User>();
	
	@PostConstruct
	public void init() {
		User user = new User();
		user.setName("User1");
		user.setLastName("Test User 1");
		users.add(user);
	}
	
}
