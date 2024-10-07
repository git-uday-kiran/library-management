package library.management.controllers;

import library.management.dtos.CreateUser;
import library.management.dtos.UserView;
import library.management.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public List<UserView> getAllUsers() {
		return userService.getAllUsers();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Long createUser(CreateUser user) {
		return userService.createUser(user);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(Long userId) {
		userService.deleteUser(userId);
	}

}
