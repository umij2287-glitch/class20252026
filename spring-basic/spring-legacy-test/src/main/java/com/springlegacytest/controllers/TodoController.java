package com.springlegacytest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TodoController {
	
	@GetMapping(path= {"/todo"})
	public String todoScreen() {
		return "todo/todo-list";
	}
}
