package library.management.repositories;

import library.management.models.LibraryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryRecordRepo extends JpaRepository<LibraryRecord, Long> {

	List<LibraryRecord> findAllByUserIdAndAndBookId(long userId, long bookId);

	List<LibraryRecord> findAllByUserId(Long userId);

}
