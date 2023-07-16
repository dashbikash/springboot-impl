package dashbikash.spring.tokenauth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
	
	@GetMapping("/private/api1")
	public ResponseEntity<Object> privateApi1(){
		return ResponseEntity.ok("Private api 1");
	}
	@GetMapping("/private/api2")
	public ResponseEntity<Object> privateApi2(){
		return ResponseEntity.ok("Private api 2");
	}
	@GetMapping("/public/api1")
	public ResponseEntity<Object> publicApi1(){
		return ResponseEntity.ok("Public api 1");
	}
	@GetMapping("/public/api2")
	public ResponseEntity<Object> publicApi2(){
		return ResponseEntity.ok("Public api 2");
	}
	
	@GetMapping("/authenticate")
	public ResponseEntity<Object> authenticate(){
		return ResponseEntity.ok("This is private api 1");
	}
	
}
