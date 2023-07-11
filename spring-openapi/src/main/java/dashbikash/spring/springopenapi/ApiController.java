package dashbikash.spring.springopenapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import dashbikash.spring.springopenapi.api.EmployeeApi;
import dashbikash.spring.springopenapi.model.Employee;

@RestController
public class ApiController implements EmployeeApi{
	@Value("classpath:data/employee.json")
	Resource resourceFile;
	
	@Override
	public ResponseEntity<Employee> getEmployeeById(String id) throws Exception {
		// TODO Auto-generated method stub
		
		return ResponseEntity.ok(this.getEmpList().stream().filter(e->e.getId().equalsIgnoreCase(id)).findFirst().get());
	}

	@Override
	public ResponseEntity<List<Employee>> getEmployeeList() throws Exception {
		// TODO Auto-generated method stub
		return ResponseEntity.ok(this.getEmpList());
	}
	private List<Employee> getEmpList() throws Exception {
		return new ObjectMapper().readValue(resourceFile.getContentAsByteArray(),new TypeReference<List<Employee>>() {
		});
	}

}
