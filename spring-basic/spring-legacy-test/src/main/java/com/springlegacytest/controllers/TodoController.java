package com.springlegacytest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.springlegacytest.dto.TodoDto;
import com.springlegacytest.mapper.TodoMapper;

@Controller
public class TodoController {
	
	private TodoMapper todoMapper;
	public TodoController(TodoMapper todoMapper) {
		this.todoMapper = todoMapper;
	}
	
	@GetMapping(path = {"/todo/list"})
	public String todoPage(Model model) {
		
		List<TodoDto> todos = todoMapper.viewTodo();
		model.addAttribute("todos", todos);
		return "todo/list";
	}
	
	@GetMapping(path = {"/todo/write"})
	public String newTodo() {
		return "todo/write";
	}
	
	@GetMapping(path = {"/todo/detail/{idx}"})
	public String todoDetail(@PathVariable("idx") int idx, Model model) {
		TodoDto todo = todoMapper.viewDetail(idx);
		model.addAttribute("todo", todo);
		return "todo/detail";
	}
	
	@PostMapping(path = {"/todo/write"})
	public String todoWrite(TodoDto todo){
		todoMapper.addTodo(todo);
		return "redirect:list";
	}
	
	@GetMapping(path = {"/todo/edit/{idx}"})
	public String todoEditPage(@PathVariable("idx") int idx, Model model) {
		model.addAttribute("todo", todoMapper.viewDetail(idx));
		return "/todo/edit";
	}
	
	@PostMapping(path = {"/todo/edit/{idx}"})
	public String todoEdit(@PathVariable("idx") int idx, 
						   @ModelAttribute TodoDto todo) {
		todoMapper.editDetail(todo);
		return "redirect:/todo/detail/" + idx;
	}
	
	@GetMapping(path = {"/todo/delete/{idx}"})
	public String todoDelete(@PathVariable("idx") int idx) {
		todoMapper.deleteTodo(idx);
		return "redirect:/todo/list";
	}
	
}
