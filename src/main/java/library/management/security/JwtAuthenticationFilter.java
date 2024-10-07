package library.management.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.util.Assert.state;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final AuthenticationManager manager;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
		Optional<String> authorization = getAuthorization(request);

		if (authorization.isPresent()) {
			String auth = authorization.get();
			String[] parts = auth.split(" ");
			state(parts.length == 2, "should have two parts in authorization header");

			String bearer = parts[0];
			String token = parts[1];
			state("Bearer".equals(bearer), "Authentication header should start wil Bearer");

			JwtAuthentication unauthenticated = JwtAuthentication.unauthenticated(token);
			Authentication authenticated = manager.authenticate(unauthenticated);
			if (authenticated == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Detail: Authentication failed");
			}

			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(authenticated);
			SecurityContextHolder.setContext(context);
		}
		filterChain.doFilter(request, response);
	}

	private Optional<String> getAuthorization(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		return Optional.ofNullable(authorization);
	}

}
