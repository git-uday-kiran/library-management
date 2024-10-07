package library.management.services;

import library.management.dtos.CreateBook;
import library.management.dtos.UpdateBook;
import library.management.models.Book;
import library.management.dtos.BookView;
import library.management.repositories.BookRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class BookService {

	private final BookRepo repo;

	public List<BookView> findAllBooks() {
		return repo.findAll().stream()
				.map(BookView::from)
				.toList();
	}

	public Long creatBook(CreateBook createBook) {
		Book newBook = new Book(createBook.getTitle(), createBook.getAuthor(), createBook.getPublishedYear());
		Book createdBook = repo.saveAndFlush(newBook);
		return createdBook.getId();
	}

	public void updateBook(UpdateBook updateBook) {
		repo.findById(updateBook.getId())
				.ifPresent(updateBookConsumer(updateBook));
	}

	public void removeBook(Long id) {
		repo.deleteById(id);
	}

	public void setStatus(Long id, Book.Status status) {
		repo.findById(id).ifPresent(setStatus(status));
	}

	private Consumer<Book> setStatus(Book.Status status) {
		return book -> {
			book.setStatus(status);
			repo.saveAndFlush(book);
		};
	}

	private static Consumer<Book> updateBookConsumer(UpdateBook updateBook) {
		return book -> {
			book.setTitle(updateBook.getTitle());
			book.setAuthor(updateBook.getAuthor());
			book.setPublishedYear(updateBook.getPublishedYear());
			book.setStatus(updateBook.getStatus());
		};
	}

}
