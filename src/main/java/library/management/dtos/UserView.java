package library.management.dtos;

import library.management.models.Role;
import library.management.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserView {

	String name;
	String password;
	Role role;

	public static UserView from(User user) {
		return new UserView(
				user.getUsername(),
				user.getPassword(),
				user.getRole()
		);
	}
}
