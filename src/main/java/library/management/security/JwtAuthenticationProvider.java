package library.management.security;

import io.jsonwebtoken.Claims;
import library.management.models.Role;
import library.management.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final JwtService jwtService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JwtAuthentication unauthenticated = (JwtAuthentication) authentication;
		Claims claims = jwtService.validateToken(unauthenticated.getToken());

		if (claims != null) {
			String username = claims.get("username", String.class);
			Role role = Role.valueOf(claims.get("role", String.class));
			return JwtAuthentication.authenticated(username, List.of(role));
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthentication.class.isAssignableFrom(authentication);
	}

}
