package com.springboot.primefaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
	    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
	})//disable auto security config in spring boot even though dependency included
public class PrimeFacesApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PrimeFacesApplication.class, args);
	}

}
