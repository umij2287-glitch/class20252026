package com.springweb.myweb.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springweb.myweb.dto.TodoDto;
import com.springweb.myweb.entity.TodoEntity;
import com.springweb.myweb.service.TodoService;

@Controller
@RequestMapping(path = "/todo")
public class TodoController {
	
	private TodoService todoService;
	public TodoController(TodoService todoService) {
		this.todoService = todoService;
	} // 의존 주입.

	@GetMapping(path = { "list" }) // @GetMapping(path = { "/todo/list" })
	public String todoList(Model model) {
		List<TodoEntity> todoList = todoService.viewTodo();
		model.addAttribute("todoList", todoList);
		// System.out.println(todoList);
		return "todo/list"; 
	}
	
	@GetMapping(path = { "write" })
	public String todoWritePage() {
		return "todo/write"; 
	}
	
	@PostMapping(path = { "write" })
	public String todoWrite(TodoDto todo) {
		todoService.writeTodo(todo);
		return "redirect:list"; 
	}
}
