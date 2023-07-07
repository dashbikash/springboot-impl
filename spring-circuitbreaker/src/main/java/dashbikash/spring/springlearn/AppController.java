package dashbikash.spring.springlearn;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class AppController {
	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping("/emps")
	@CircuitBreaker(fallbackMethod = "fallback", name = "mymethod")
	public ResponseEntity<Object> emps(){
		return ResponseEntity.ok(restTemplate.getForEntity("http://localhost:3000/employees/1", Employee.class).getBody());
	}
	
	public ResponseEntity<Object> fallback(Throwable throwable){
		Employee newEmp= new Employee( "Bikash", "Dash","bikash@gmail.com",36,12,2400000 ,"male","8917338883", "Software Engineering","IT" );
		newEmp.setId(UUID.randomUUID().toString());
		return ResponseEntity.ok(newEmp);
	}
}
