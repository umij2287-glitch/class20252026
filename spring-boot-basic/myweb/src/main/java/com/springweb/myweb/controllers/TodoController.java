package com.springweb.myweb.controllers;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springweb.myweb.dto.TodoDto;
import com.springweb.myweb.service.TodoService;

@Controller
@RequestMapping(path = "/todo")
public class TodoController {
	
	private TodoService todoService;
	public TodoController(TodoService todoService) {
		this.todoService = todoService;
	} // 의존 주입.

	@GetMapping(path = { "list" }) // @GetMapping(path = { "/todo/list" }) ->requestMapping 으로 인해 /todo 가 앞에 붙음.
	public String todoList(Model model) {
		List<TodoDto> todoList = todoService.viewTodo();
		model.addAttribute("todoList", todoList);
		//System.out.println(todoList);
		return "todo/list"; 
	}
	
	// Todo 작성 페이지.
	@GetMapping(path = { "write" })
	public String todoWritePage() {
		return "todo/write"; 
	}
	
	// Todo 작성 등록.
	@PostMapping(path = { "write" })
	public String todoWrite(TodoDto todo) {
		todoService.writeTodo(todo);
		return "redirect:list"; 
	}
	
	// Todo 상세 보기.
	@GetMapping(path = { "detail/{idx}" })
	public String todoDetail(@PathVariable("idx") int idx, Model model) {
		TodoDto todo = todoService.viewDetail(idx);
		if (todo == null) {
			return "redirect:todo/list"; 
		}
		model.addAttribute("todo", todo);
		return "todo/detail"; 
	}
	
	// Todo 삭제.
	@GetMapping(path = { "delete/{idx}" })
	public String deleteTodo(@PathVariable("idx") int idx) {
		 todoService.deleteTodo(idx);
		return "redirect:/todo/list"; 
	}
	
	// Todo 수정 페이지.
	@GetMapping(path = { "edit" })
	public String editPage(@RequestParam("idx") int idx,Model model) {
		TodoDto todo = todoService.viewDetail(idx);
		if (todo == null) {
			return "redirect:todo/list"; 
		}
		model.addAttribute("todo", todo);
		return "todo/edit"; 
	}
	
	// Todo 수정 적용.
	@PostMapping(path = { "edit" })
	public String editTodo(TodoDto todo) {
		todoService.updateTodo(todo);
		return "redirect:/todo/detail/" + todo.getIdx(); 
	}
}
