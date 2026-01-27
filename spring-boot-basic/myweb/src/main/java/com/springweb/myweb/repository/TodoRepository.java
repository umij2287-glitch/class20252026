package com.springweb.myweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springweb.myweb.entity.TodoEntity;

public interface TodoRepository extends JpaRepository<TodoEntity, Integer>{
	// TodoEntity 클래스의 정보를 사용해서 데이터베이스의 테이블의 데이터를 CRUD할 수 있는 메서드들을 자동으로 사용할 수 있게 해줌
}
