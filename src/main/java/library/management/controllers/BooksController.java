package library.management.controllers;

import library.management.dtos.CreateBook;
import library.management.dtos.UpdateBook;
import library.management.dtos.BookView;
import library.management.services.BookService;
import library.management.services.LibraryRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {

	private final BookService bookService;
	private final LibraryRecordService recordService;

	@GetMapping
	public List<BookView> viewBooks() {
		return bookService.findAllBooks();
	}

	@PostMapping
	public Long add(CreateBook book) {
		return bookService.creatBook(book);
	}

	@PutMapping
	public void update(UpdateBook book) {
		bookService.updateBook(book);
	}

	@DeleteMapping
	public void delete(Long bookId) {
		bookService.removeBook(bookId);
	}

	@PostMapping("/borrow/{bookId}/{userId}")
	public void borrowBook(@PathVariable Long bookId, @PathVariable Long userId) {
		recordService.borrowBook(bookId, userId);
	}

	@PostMapping("/return/{userId}")
	public void returnBooks(@RequestBody List<Long> bookIds, @PathVariable Long userId) {
		recordService.returnBooks(userId, bookIds);
	}

}
