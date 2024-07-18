package dashbikash.spring.spring_native;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
	@GetMapping("/index")
	public ResponseEntity<Object> index(){
		return ResponseEntity.ok("Hello Spring Native");
	}
}
