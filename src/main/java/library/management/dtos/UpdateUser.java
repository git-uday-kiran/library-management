package library.management.dtos;

import library.management.models.Role;
import lombok.Data;

@Data
public class UpdateUser {

	Long id;
	String name;
	String password;
	Role role;

}
