package com.practice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
// component scanning 이 되어 있어야 가능. (servlet-context.xml)
public class UserController {

	@GetMapping(path = {"/account/login"})
	public String loginScreen() {
		return "account/login";  
		// servlet-context.xml 에서 InternalSourceViewResolver 에 의해
		// prefix(/WEB-INF/views/), suffix(.jsp) 가 설정되어 있음.
	}
	
	@GetMapping(path = {"/account/register"})
	public String registerScreen() {
		return "account/register";
	}
	
	@GetMapping(path = {"/board/list"})
	public String boardScreen() {
		return "board/list";
	}
}
