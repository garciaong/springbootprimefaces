package com.springboot.primefaces.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VueWebController {

	@GetMapping(value="/csrf")
	public String loadCsrfPage(Model model) {
		return "vuecsrf";
	}
}
