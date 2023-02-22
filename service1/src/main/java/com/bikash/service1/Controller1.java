package com.bikash.service1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller1 {
	@GetMapping("/")
	public String hello() {
		return "Hello from Service 1";
	}
}
