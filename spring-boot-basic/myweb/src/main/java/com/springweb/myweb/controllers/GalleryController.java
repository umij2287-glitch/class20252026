package com.springweb.myweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = {"/gallery"})
public class GalleryController {
	
	@GetMapping("/list")
	public String galleryList() {
		return "gallery/list";
	}
	
	// 사진 등록 페이지
	@GetMapping("/register")
	public String galleryRegisterPage() {
		return "gallery/register";
	}
}
