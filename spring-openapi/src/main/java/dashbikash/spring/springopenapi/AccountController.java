package dashbikash.spring.springopenapi;

import java.security.MessageDigest;
import java.util.Base64;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import dashbikash.spring.springopenapi.api.AccountApi;
import dashbikash.spring.springopenapi.model.AuthUser;
import jakarta.validation.Valid;

@RestController
public class AccountController implements AccountApi{
	@Override
	public ResponseEntity<String> authenticate(@Valid AuthUser authUser) throws Exception {
		if(authUser.getUsername().equalsIgnoreCase("user") && authUser.getPasswd().equals("password"))
		{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] authByte=digest.digest(String.join("|",authUser.getUsername(),authUser.getPasswd()).getBytes());
			return ResponseEntity.ok().header("Authorization",Base64.getEncoder().encodeToString(authByte)).build();
		}else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authentication Failed");
		}
		
	}

}
