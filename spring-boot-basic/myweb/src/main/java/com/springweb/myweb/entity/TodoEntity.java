package com.springweb.myweb.entity;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity // database table과 매핑되는 클래스임을 나타냄.
@Table(name = "tbl_todo") // 매핑할 테이블 이름 지정
@Data
public class TodoEntity {
	
	@Id // primary key 지정
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 설정: mySQL 기준, Oracle은 SEQUENCE 사용.
	private int idx;
	
	@Column(length = 200, nullable = false) // null 허용 여부 지정
	private String title;
	
	@Column(nullable = false, length = 1000)
	private String content;
	
	@Column
	private Date writeDate = new Date(); // 기본값 현재 날짜로 설정
	
	@Column
	private boolean completed = false; // 기본값 설정
}
