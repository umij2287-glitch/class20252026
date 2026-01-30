package com.springweb.myweb.entity;

import java.util.Date;

import com.springweb.myweb.dto.GalleryDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_gallery")
public class GalleryEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idx;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Column(nullable = false, length = 500)
	private String description;
	
	@Column(nullable = false)
	private String originalFileName;
	
	@Column(nullable = false)
	private String savedFileName;
	
	@Column(nullable = false)
	private Date registerDate = new Date();
	
	@Column(nullable = false)
	private boolean deleted = false;
	
	public static GalleryEntity toEntity(GalleryDto galleryDto) {
		
		GalleryEntity galleryEntity = new GalleryEntity();
		galleryEntity.setTitle(galleryDto.getTitle());
		galleryEntity.setDescription(galleryDto.getDescription());
		galleryEntity.setOriginalFileName(galleryDto.getOriginalFileName());
		galleryEntity.setSavedFileName(galleryDto.getSavedFileName());
		return galleryEntity;
	}
	
	public GalleryDto toDto() {
		
		GalleryDto galleryDto = new GalleryDto();
		galleryDto.setIdx(idx);
		galleryDto.setTitle(title);
		galleryDto.setDescription(description);
		galleryDto.setOriginalFileName(originalFileName);
		galleryDto.setSavedFileName(savedFileName);
		galleryDto.setRegisterDate(registerDate);
		galleryDto.setDeleted(deleted);
		return galleryDto;
	}
}
