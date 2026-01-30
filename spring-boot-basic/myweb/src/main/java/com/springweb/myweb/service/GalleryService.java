package com.springweb.myweb.service;

import java.util.List;

import com.springweb.myweb.dto.GalleryDto;


public interface GalleryService {

	void registerGallery(GalleryDto galleryDto);

	List<GalleryDto> findAll();

	GalleryDto findByIdx(int idx);

	void update(GalleryDto galleryDto);

}
