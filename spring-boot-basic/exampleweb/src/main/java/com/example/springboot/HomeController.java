package com.example.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@GetMapping(path = {"/", "/home" })
	public String home() {
		return "home"; // -> "templates/" + home + ".html"
	}
}
