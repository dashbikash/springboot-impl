package dashbikash.spring.tokenauth;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class AuthToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private String apiKey;
	public AuthToken(String apiToken ,Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.apiKey=apiToken;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPrincipal() {
		// TODO Auto-generated method stub
		return this.apiKey;
	}

}
