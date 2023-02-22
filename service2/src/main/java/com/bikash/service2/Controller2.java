package com.bikash.service2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller2 {
	@GetMapping("/")
	public String hello() {
		return "Hello from Service 2";
	}
}
