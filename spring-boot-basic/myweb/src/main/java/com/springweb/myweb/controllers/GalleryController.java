package com.springweb.myweb.controllers;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springweb.myweb.common.Util;
import com.springweb.myweb.dto.GalleryDto;
import com.springweb.myweb.service.GalleryService;

@Controller // IoC 컨테이너에 컨트롤러 클래스로 등록.
@RequestMapping(path = {"/gallery"})
public class GalleryController {
	
	// DI(Dependency Injection) : 생성자 의존 주입.
	private GalleryService galleryService;
	public GalleryController(GalleryService galleryService) {
		this.galleryService = galleryService;
	}

	@GetMapping(path = {"/list"})
	public String galleryList(Model model) {
		List<GalleryDto> galleryList = galleryService.findAll();
		model.addAttribute("galleryList", galleryList);
		return "gallery/list";
	}
	
	// 사진 등록 페이지
	@GetMapping(path = {"/register"})
	public String galleryRegisterPage() {
			
		return "gallery/register";
	}
	
	// 사진 등록 처리
	final String UPLOAD_PATH = "D:\\uploaded";
	
	@PostMapping(path = {"/register"})
	public String galleryRegister(
			
			@RequestParam("picture") MultipartFile picture, 
			GalleryDto galleryDto) {
		
		// if (!picture.isEmpty()) 시도
		if (picture != null && picture.getOriginalFilename().length() > 0)	{
			String savedFileName = Util.makeUniqueFileName(picture.getOriginalFilename());
			// System.out.println(picture.getOriginalFilename());
			// System.out.println(savedFileName);
			File file = new File(UPLOAD_PATH, savedFileName);
			try {
				galleryDto.setOriginalFileName(picture.getOriginalFilename());
				galleryDto.setSavedFileName(savedFileName);
				
				galleryService.registerGallery(galleryDto);
				
				picture.transferTo(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:list";
	}
	
	@GetMapping(path = {"/detail/{idx}"})
	public String galleryDetail(@PathVariable("idx") int idx, Model model) {
		
		GalleryDto galleryDto = galleryService.findByIdx(idx);
		if (galleryDto == null) {
			return "redirect:list";
		}
		model.addAttribute("galleryDto", galleryDto);
		return "gallery/detail";
	}
	
	@GetMapping(path = {"/edit/{idx}"})
	public String galleryEditPage(@PathVariable("idx") int idx, Model model) {
		GalleryDto galleryDto = galleryService.findByIdx(idx);
		model.addAttribute("galleryDto", galleryDto);
		return "gallery/edit";
	}
	
	@PostMapping(path = {"/edit/{idx}"})
	public String galleryEdit(
			@PathVariable("idx") int idx,
			@RequestParam("picture") MultipartFile picture,
			GalleryDto galleryDto) {
		galleryService.update(galleryDto);
		return "redirect:detail/" + galleryDto.getIdx();
	}
}
