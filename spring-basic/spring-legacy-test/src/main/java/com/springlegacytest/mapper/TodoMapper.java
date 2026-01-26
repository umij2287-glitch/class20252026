package com.springlegacytest.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springlegacytest.dto.TodoDto;

@Mapper
public interface TodoMapper {
	
	void addTodo(TodoDto todo);
	
	List<TodoDto> viewTodo();
	
	TodoDto viewDetail(@Param("idx")int idx);
	
	void editDetail(TodoDto todo);
	
	
}
