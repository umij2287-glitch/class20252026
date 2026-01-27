package com.springweb.myweb.dto;

import java.util.Date;

import lombok.Data;

@Data
public class TodoDto {
	private int idx;
	private String title;
	private String content;
	private Date writeDate;
	private boolean completed;
}
