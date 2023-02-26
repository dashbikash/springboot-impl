package com.bikash.service1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.micrometer.core.ipc.http.HttpSender.Response;
import reactor.core.publisher.Mono;

@RestController
public class Controller1 {
	@Autowired
	@Lazy
	RestTemplate restTemplate;
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello from Service 1";
	}
	
	
	@GetMapping("/call-cb")
	@CircuitBreaker( name = "CircuitBreakerService",fallbackMethod = "fallback")
	public ResponseEntity<String> callcb() {
		
		return restTemplate.getForEntity("http://localhost:8080/cb",String.class);
	}
	
	
	@GetMapping("/cb")
	public Mono<String> circuitBreakerApi() {
		return Mono.error(new Exception("Hello Circuit Breaker"));
	}
	public Mono<String>  fallback(Exception e){
		System.out.println("Circuit breaker fallback");
		//System.out.println(e);
	    return Mono.just("Hello fallback");
	}
	
}

