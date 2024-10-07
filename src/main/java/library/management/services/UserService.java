package library.management.services;

import library.management.dtos.CreateUser;
import library.management.dtos.UpdateUser;
import library.management.dtos.UserView;
import library.management.models.Role;
import library.management.models.User;
import library.management.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Predicate;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepo repo;
	private final LibraryRecordService recordService;

	public Long createUser(CreateUser createUser) {
		User newUser = new User(createUser.getUsername(), createUser.getPassword(), createUser.getRole());
		User createdUser = repo.saveAndFlush(newUser);
		return createdUser.getId();
	}

	public void deleteUser(Long userId) {
		repo.findById(userId).ifPresent(user -> {
			user.setStatus(User.Status.DELETED);
			recordService.deletedUser(user.getId());
			repo.saveAndFlush(user);
		});
	}

	public void updateUser(UpdateUser updateUser) {
		repo.findById(updateUser.getId()).ifPresent(user -> {
			user.setUsername(updateUser.getName());
			user.setPassword(updateUser.getPassword());
			user.setRole(updateUser.getRole());
		});
	}

	public void updateMember(UpdateUser updateUser) {
		repo.findById(updateUser.getId())
				.filter(isMember())
				.ifPresent(user -> {
					user.setUsername(updateUser.getName());
					user.setPassword(updateUser.getPassword());
				});
	}

	public Long addMember(CreateUser createUser) {
		Assert.state(createUser.getRole() == Role.MEMBER, "role must be a MEMBER");
		return createUser(createUser);
	}

	public void deleteMember(Long userId) {
		repo.findById(userId)
				.filter(isMember())
				.map(User::getId)
				.ifPresent(this::deleteUser);
	}

	public List<UserView> getAllUsers() {
		return repo.findAll().stream()
				.map(UserView::from)
				.toList();
	}

	public List<UserView> getAllMembers() {
		return repo.findAll().stream()
				.filter(isMember())
				.map(UserView::from)
				.toList();
	}

	public List<UserView> getDeletedMembers() {
		return repo.findAll().stream()
				.filter(isMember())
				.filter(isDeleted())
				.map(UserView::from)
				.toList();
	}

	public List<User> getActiveMembers() {
		return repo.findAll().stream()
				.filter(isMember())
				.filter(isActive())
				.toList();
	}

	private static Predicate<User> isActive() {
		return user -> user.getStatus() == User.Status.ACTIVE;
	}

	private static Predicate<User> isDeleted() {
		return user -> user.getStatus() == User.Status.DELETED;
	}

	private static Predicate<User> isMember() {
		return user -> user.getRole().equals(Role.MEMBER);
	}


}
