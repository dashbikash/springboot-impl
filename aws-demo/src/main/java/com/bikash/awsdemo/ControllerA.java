package com.bikash.awsdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class ControllerA {
	
	@GetMapping("/")
	public Mono<String> index(){
		return Mono.just("Hello AWS , From Springboot");
	}
	
}
