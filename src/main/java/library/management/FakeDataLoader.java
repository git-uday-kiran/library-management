package library.management;

import com.github.javafaker.Faker;
import library.management.dtos.CreateUser;
import library.management.models.Role;
import library.management.services.BookService;
import library.management.services.LibraryRecordService;
import library.management.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Log4j2
@Component
@RequiredArgsConstructor
public class FakeDataLoader implements ApplicationRunner {

	private final UserService userService;
	private final BookService bookService;
	private final LibraryRecordService recordService;
	private final Faker faker = new Faker();


	@Override
	public void run(ApplicationArguments args) throws Exception {
		loadUsers();
	}

	void loadUsers() {
		Role[] roles = Role.values();

		String adminUsername = "admin";
		String adminPassword = "admin";
		Role adminRole = Role.LIBRARIAN;
		userService.createUser(new CreateUser(adminUsername, adminPassword, adminRole));

		IntStream.range(0, 10).forEach(i -> {
			String username = faker.name().firstName();
			String password = faker.crypto().md5();
			Role role = roles[random(roles.length)];
			userService.createUser(new CreateUser(username, password, role));
		});
	}

	int random(int max) {
		return faker.random().nextInt(max);
	}

}
