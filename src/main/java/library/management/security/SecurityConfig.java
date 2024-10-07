package library.management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationProvider provider) throws Exception {
		ProviderManager manager = new ProviderManager(provider);
		JwtAuthenticationFilter jwtAuthFilter = new JwtAuthenticationFilter(manager);

		return http
				.securityMatcher("/users/**", "/members/**", "/books/**")
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(config -> {
					config.requestMatchers("/singup", "/signin").permitAll();
					config.anyRequest().authenticated();
				})
				.authenticationManager(manager)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

}
