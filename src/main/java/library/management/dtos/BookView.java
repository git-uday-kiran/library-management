package library.management.dtos;

import library.management.models.Book;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookView {

	String title;
	String author;
	Integer publishedYear;
	Book.Status status;

	public static BookView from(Book book) {
		return new BookView(
				book.getTitle(),
				book.getAuthor(),
				book.getPublishedYear(),
				book.getStatus()
		);
	}

}
