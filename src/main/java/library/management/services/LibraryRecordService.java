package library.management.services;

import library.management.dtos.LibraryRecordView;
import library.management.models.Book;
import library.management.models.LibraryRecord;
import library.management.models.User;
import library.management.repositories.BookRepo;
import library.management.repositories.LibraryRecordRepo;
import library.management.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryRecordService {

	private final LibraryRecordRepo recordRepo;
	private final UserRepo userRepo;
	private final BookService bookService;
	private final BookRepo bookRepo;

	public LibraryRecord save(LibraryRecord record) {
		return recordRepo.saveAndFlush(record);
	}

	public List<LibraryRecordView> getHistory() {
		return recordRepo.findAll().stream()
				.map(LibraryRecordView::from)
				.toList();
	}

	public void borrowBook(Long userId, Long bookId) {
		var opUser = userRepo.findById(userId);
		var opBook = bookRepo.findById(bookId);

		if (opUser.isPresent() && opBook.isPresent() && opBook.get().getStatus() == Book.Status.AVAILABLE) {
			User user = opUser.get();
			Book book = opBook.get();

			LibraryRecord newRecord = new LibraryRecord(user, book);
			recordRepo.saveAndFlush(newRecord);
			bookService.setStatus(book.getId(), Book.Status.BORROWED);
		}
	}

	public void deletedUser(Long userId) {
		recordRepo.findAllByUserId(userId).forEach(record -> {
			bookService.setStatus(record.getBook().getId(), Book.Status.AVAILABLE);
		});
	}

	public void returnBooks(Long userId, List<Long> bookIds) {
		bookIds.forEach(bookId -> returnBook(userId, bookId));
	}

	public void returnBook(long userId, long bookId) {
		recordRepo.findAllByUserIdAndAndBookId(userId, bookId).stream()
				.filter(record -> record.getReturnedAt() == null)
				.findFirst()
				.ifPresent(record -> {
					record.setReturnedAt(LocalDateTime.now());
					recordRepo.saveAndFlush(record);
					bookService.setStatus(record.getBook().getId(), Book.Status.AVAILABLE);
				});
	}

}
