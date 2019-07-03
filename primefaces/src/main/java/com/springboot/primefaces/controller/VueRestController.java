package com.springboot.primefaces.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.primefaces.model.Request;
import com.springboot.primefaces.model.Response;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class VueRestController {

	@PostMapping(value="/testcsrf", produces = "application/json")
	public ResponseEntity<Response> testCsrf(@RequestBody Request request) {
		try {
			log.info("Invoking '/testcsrf' endpoint");
			log.info("Info : "+request.getInfo());
			Response response = new Response(HttpStatus.OK,"If you see this, congratulation...");
			
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		} catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<Response>(new Response(HttpStatus.INTERNAL_SERVER_ERROR,"Endpoint invocation failed! Error : ["+e+"]"), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
    }
	
}
