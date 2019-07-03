package com.springboot.primefaces.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Request {

	@JsonProperty(value = "info")
	@JsonInclude(Include.NON_NULL)
	private String info;
	
}
