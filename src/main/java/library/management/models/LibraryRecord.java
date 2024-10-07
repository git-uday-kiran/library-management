package library.management.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LibraryRecord extends BaseId {

	@ManyToOne
	User user;

	@ManyToOne
	Book book;

	@Column(nullable = false)
	LocalDateTime issuedAt;

	LocalDateTime returnedAt;

	@PrePersist
	public void prePersist() {
		this.issuedAt = LocalDateTime.now();
	}

	public LibraryRecord(User user, Book book) {
		this.user = user;
		this.book = book;
	}

}
