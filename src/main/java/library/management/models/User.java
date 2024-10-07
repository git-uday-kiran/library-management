package library.management.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseId {

	@Column(nullable = false, unique = true)
	String username;

	@Column(nullable = false)
	String password;

	@Column(nullable = false)
	Role role;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	Status status = Status.ACTIVE;

	public User(String username, String password, Role role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public enum Status {
		ACTIVE, INACTIVE, DELETED
	}
}
