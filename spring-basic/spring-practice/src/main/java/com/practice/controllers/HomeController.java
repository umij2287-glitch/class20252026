package com.practice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // component scanning is needed !
public class HomeController {

	@GetMapping(path = {"/", "/home"})
	public String home() {
		// InternalResourceViewResolver 에 의해 servlet-context.xml 에서 지정된 prefix, suffix
		// /WEB-INF/views/home.jsp
		return "home"; 
	}
}
