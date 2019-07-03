package com.springboot.primefaces.model;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Response {

	@JsonProperty(value = "status")
	@JsonInclude(Include.NON_NULL)
	private String status;
	@JsonProperty(value = "content")
	@JsonInclude(Include.NON_NULL)
	private String message;
	
	public Response() {}
	public Response(HttpStatus status,String message) {
		this.status = String.valueOf(status.value());
		this.message = message;
	}
}
