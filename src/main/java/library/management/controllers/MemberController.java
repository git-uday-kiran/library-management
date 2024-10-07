package library.management.controllers;

import library.management.dtos.CreateUser;
import library.management.dtos.LibraryRecordView;
import library.management.dtos.UpdateUser;
import library.management.dtos.UserView;
import library.management.services.LibraryRecordService;
import library.management.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

	private final UserService userService;
	private final LibraryRecordService recordService;

	@GetMapping
	public List<UserView> getAllMembers() {
		return userService.getAllMembers();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Long addMember(CreateUser user) {
		return userService.addMember(user);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateMember(UpdateUser member) {
		userService.updateMember(member);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public void deleteMember(Long id) {
		userService.deleteMember(id);
	}

	@GetMapping("/deleted")
	public List<UserView> getDeletedMembers() {
		return userService.getDeletedMembers();
	}

	@GetMapping("/active")
	public List<UserView> getActiveMembers() {
		return userService.getAllMembers();
	}

	@GetMapping("/history")
	public List<LibraryRecordView> getMembersRecordHistory() {
		return recordService.getHistory();
	}

}
