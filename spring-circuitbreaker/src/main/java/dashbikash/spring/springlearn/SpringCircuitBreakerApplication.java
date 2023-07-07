package dashbikash.spring.springlearn;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SpringCircuitBreakerApplication implements CommandLineRunner{

	
	
	public static void main(String[] args) {
		SpringApplication.run(SpringCircuitBreakerApplication.class, args);
		
	}
	
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	@Bean
	public RestTemplate getRestTemplate() {
//	    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
//	      = new HttpComponentsClientHttpRequestFactory();
//	    //clientHttpRequestFactory.setConnectTimeout(timeout);
		return new RestTemplateBuilder().build();
	}


}
