package library.management.dtos;

import library.management.models.LibraryRecord;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LibraryRecordView {

	String user;

	String bookTitle;

	LocalDateTime issuedAt;

	LocalDateTime returnedAt;


	public static LibraryRecordView from(LibraryRecord libraryRecord) {
		return new LibraryRecordView(
				libraryRecord.getUser().getUsername(),
				libraryRecord.getBook().getTitle(),
				libraryRecord.getIssuedAt(),
				libraryRecord.getReturnedAt()
		);
	}
}
