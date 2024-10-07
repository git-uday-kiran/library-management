package library.management.controllers;

import library.management.dtos.SignInRequest;
import library.management.dtos.SignInResponse;
import library.management.security.JwtService;
import library.management.services.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signin")
@RequiredArgsConstructor
public class SignInController {

	private final SignInService signInService;
	private final JwtService jwtService;

	@PostMapping
	SignInResponse signIn(@RequestBody SignInRequest request) {
		return signInService.signIn(request);
	}

}
