package library.management.services;

import library.management.dtos.SignInRequest;
import library.management.dtos.SignInResponse;
import library.management.models.User;
import library.management.repositories.UserRepo;
import library.management.security.JwtService;
import library.management.utils.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.net.PasswordAuthentication;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class SignInService {

	private final UserRepo userRepo;
	private final JwtService jwtService;

	public SignInResponse signIn(SignInRequest request) {
		String username = request.getUsername();
		String password = request.getPassword();
		var opUser = userRepo.findByUsernameAndPassword(username, password);
		if (opUser.isPresent()) {
			User user = opUser.get();
			String token = jwtService.generateToken(authentication(user), claims(user));
			log.info("Generated token: {}", token);
			return new SignInResponse(token);
		}
		throw new ResourceNotFoundException("User credentials are not found in our records");
	}

	private PasswordAuthentication authentication(User user) {
		return new PasswordAuthentication(user.getUsername(), user.getPassword().toCharArray());
	}

	private Map<String, Object> claims(User user) {
		return Map.of("role", user.getRole());
	}

}
