package dashbikash.spring.jwtauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
	@Autowired
	JwtService jwtService;
	
	@Autowired
	UserDetailsService detailsService;
	
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
	
	@PostMapping(path="/authenticate",consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> authenticate(@RequestBody Map<String, String> credentials){
		UserDetails userDetails= detailsService.loadUserByUsername(credentials.get("username"));

	
		if(userDetails!=null && userDetails.getPassword().equals(credentials.get("password"))) {
			return ResponseEntity.ok(jwtService.generateToken(credentials.get("username")));
		}else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials.");
		}
		
	}
	
	
}
