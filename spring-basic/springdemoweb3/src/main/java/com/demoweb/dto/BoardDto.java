package com.demoweb.dto;

import java.util.ArrayList;
import java.util.Date;

import lombok.Data;

@Data // 자동으로 모든 필드의 getter, setter 생성 
public class BoardDto {
	
	private int boardNo;
	private String writer;
	private String title;
	private String content;
	private Date writeDate;
	private Date modifyDate;
	private int readCount;
	private String category;
	private boolean deleted;
	// 데이터베이스 테이블 사이의 1:many 의 관계를 구현하는 필드.
	private ArrayList<BoardAttachDto> attachments;
}
