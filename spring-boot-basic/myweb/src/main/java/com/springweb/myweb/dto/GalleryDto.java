package com.springweb.myweb.dto;

import java.util.Date;

import lombok.Data;

@Data
public class GalleryDto {
	
	private int idx;
	private String title;
	private String description;
	private String originalFileName;
	private String savedFileName;
	private Date registerDate;
	private boolean deleted;
	
}
