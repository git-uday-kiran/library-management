package library.management.security;

import library.management.models.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;

import java.util.List;

@Getter
public class JwtAuthentication implements Authentication {

	private String token;
	private String username;
	private List<Role> roles;
	private boolean authenticated;

	private JwtAuthentication(String token) {
		this.token = token;
	}

	private JwtAuthentication(String username, List<Role> roles) {
		this.username = username;
		this.roles = roles;
	}

	public static JwtAuthentication unauthenticated(String token) {
		JwtAuthentication auth = new JwtAuthentication(token);
		auth.setAuthenticated(false);
		return auth;
	}

	public static JwtAuthentication authenticated(String username, List<Role> roles) {
		JwtAuthentication auth = new JwtAuthentication(username, roles);
		auth.setAuthenticated(true);
		return auth;
	}

	@Override
	public List<Role> getAuthorities() {
		return roles;
	}

	@Override
	public Object getCredentials() {
		throw new IllegalStateException("Not implemented");
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return username;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.authenticated = isAuthenticated;
	}

	@Override
	public String getName() {
		return username;
	}
}
