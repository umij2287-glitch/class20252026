package com.springweb.myweb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springweb.myweb.dto.TodoDto;
import com.springweb.myweb.entity.TodoEntity;
import com.springweb.myweb.repository.TodoRepository;

@Service // 서비스 영역의 빈을 강조하기 위해 사용하는 @Component 의 별칭.
public class TodoServiceImpl implements TodoService {
// suffix "-Impl" : 구현(Implementation)이라는 의미.
	
	private TodoRepository todoRepository;
	public TodoServiceImpl(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}
	
	@Override
	public void writeTodo(TodoDto todo) {
		TodoEntity entity = new TodoEntity();
		entity.setTitle(todo.getTitle());
		entity.setContent(todo.getContent());
		entity.setCompleted(todo.isCompleted());
		todoRepository.save(entity);
	}

	@Override
	public List<TodoEntity> viewTodo() {
		return todoRepository.findAll();
	}
}
