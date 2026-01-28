package com.springweb.myweb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		TodoEntity entity = TodoEntity.toEntity(todo);
		todoRepository.save(entity);
	}

	@Override
	public List<TodoDto> viewTodo() {
		List<TodoEntity> entityList = todoRepository.findAll();
		ArrayList<TodoDto> dtoList = new ArrayList<>();
		for(TodoEntity todoEntity : entityList) {
			TodoDto todoDto = todoEntity.toDto();
			dtoList.add(todoDto);
		}
		return dtoList;
	}

	@Override
	public TodoDto viewDetail(int idx) {
//		Optional<TodoEntity>  todoEntity = todoRepository.findById(idx);
//		if (todoEntity.isPresent()) {
//			return todoEntity.get().toDto(); // .get() : Optional 에서 실제 값을 꺼낼 때 사용.
//		}
//		return null;
		Optional<TodoEntity>  todoEntity = todoRepository.findById(idx);
		TodoEntity todoEntity2 = todoEntity.orElse(null);
		return (todoEntity2 != null ? todoEntity2.toDto() : null);
	}

	@Override
	public void deleteTodo(int idx) {
		todoRepository.deleteById(idx);
	}

	@Override
	public void updateTodo(TodoDto todo) {

//		TodoEntity entity = TodoEntity.toEntity(todo); 
//		todoRepository.save(entity);
//		insert 방식
		
//		TodoEntity entity = TodoEntity.toEntity(todo);
//		entity.setIdx(todo.getIdx()); 
//		todoRepository.save(entity);
//		위의 코드는 새로 생성하는 방식이기 때문에 기존의 createdAt 이 초기화 되는 문제가 발생.
//		update 상에서의 최적화 문제도 발생할 수 있음.
		
		// update 방식
		Optional<TodoEntity> todoEntity = todoRepository.findById(todo.getIdx());
		if (todoEntity.isPresent()) {
			TodoEntity prevEntity = todoEntity.get();
			prevEntity.setTitle(todo.getTitle());
			prevEntity.setContent(todo.getContent());
			prevEntity.setCompleted(todo.isCompleted());
			todoRepository.save(prevEntity);
		}
	}
}
