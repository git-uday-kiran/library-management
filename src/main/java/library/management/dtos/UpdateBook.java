package library.management.dtos;

import library.management.models.Book;
import lombok.Data;

@Data
public class UpdateBook {

	Long id;
	String title;
	String author;
	Integer publishedYear;
	Book.Status status = Book.Status.AVAILABLE;

}
