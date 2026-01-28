package com.springweb.myweb.service;

import java.util.List;

import com.springweb.myweb.dto.TodoDto;
import com.springweb.myweb.entity.TodoEntity;

public interface TodoService {

	void writeTodo(TodoDto todo);

	List<TodoDto> viewTodo();

	TodoDto viewDetail(int idx);

	void deleteTodo(int idx);

	void updateTodo(TodoDto todo);

}
