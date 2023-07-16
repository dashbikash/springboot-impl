package dashbikash.spring.jwtauth;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AppUserDetail implements UserDetailsService {
	@Value("classpath:data/users.json")
	private Resource usersJson;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ObjectMapper mapper = new ObjectMapper();
		List<User> users;
		try {
			users = mapper.readValue(usersJson.getContentAsByteArray(), new TypeReference<List<User>>() {
			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return users.stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst().get();

	}

}
