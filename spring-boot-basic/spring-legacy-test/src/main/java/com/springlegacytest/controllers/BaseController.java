package com.springlegacytest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {
	
	@GetMapping(path = {"/", "/home"})
	public String home() {
		return "home";
	}
}
