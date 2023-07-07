package dashbikash.spring.springmongo;

import java.util.*;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@SpringBootApplication
public class SpringmongoApplication implements CommandLineRunner{
	@Autowired
	protected MongoTemplate mongoTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringmongoApplication.class, args);
		
	}
	
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		List<Employee> emps=mongoTemplate.findAll(Employee.class,"employees");
		System.out.println(emps.stream().count());
		var cmap= emps.stream().collect(Collectors.groupingBy(Employee::getGender,Collectors.maxBy(Comparator.comparing(Employee::getSalary))))
				.entrySet().stream()
				.collect(Collectors.toMap(e->e.getKey(),e->e.getValue().get().getSalary()));		
		System.out.println(cmap);
		
		//emps.stream().sorted(Comparator.comparing(Employee::getAge)).forEach(System.out::println);
		float sal= emps.stream().map(e->e.getSalary()).distinct().sorted(Comparator.reverseOrder()).toList().get(1);
		System.out.println(sal);
	}
	
	public void find() {
		mongoTemplate.findAll(Employee.class,"employee").forEach(System.out::println);
	}
	public void findFilter() {
		Query query=new Query();
		query.addCriteria(Criteria.where("Gender").is("female"));
		query.addCriteria(Criteria.where("Experience").is(13));
		mongoTemplate.find(query,Employee.class,"employee").forEach(System.out::println);
	}
	public void replaceorCreate() {
		Query query=new Query();
		query.addCriteria(Criteria.where("FName").is("Diane"));
		query.addCriteria(Criteria.where("LName").is("Carter"));
		Employee newEmp= new Employee( "Bikash", "Dash","bikash@gmail.com",36,12,2400000 ,"male","8917338883", "Software Engineering","IT" );
		mongoTemplate.findAndReplace(query,newEmp,new FindAndReplaceOptions().upsert(),"employee");
	}


}
