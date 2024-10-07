package library.management.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Book extends BaseId {

	@Column(nullable = false, unique = true)
	String title;

	@Column(nullable = false)
	String author;

	@Column(nullable = false)
	Integer publishedYear;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	Status status = Status.AVAILABLE;

	public Book(String title, String author, Integer publishedYear) {
		this.title = title;
		this.author = author;
		this.publishedYear = publishedYear;
	}

	public enum Status {
		AVAILABLE, BORROWED
	}
}
