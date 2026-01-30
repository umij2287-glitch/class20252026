package com.springweb.myweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // web.xml, servlet-context.xml 과 같은 설정을 위한 클래스로 등록.
public class MywebConfig implements WebMvcConfigurer {
// 웹 설정 클래스 :
// WebMvcConfigurer 를 구현하여 legacy Spring-MVC 에서 web.xml 과 servelet-context.xml 설정과 같은 역활을 함.
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploaded/**")
				.addResourceLocations("file:D:/uploaded/");
	}
}
