package cc.maids.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cc.maids.entity.BorrowingRecord;
import cc.maids.entity.BorrowingRecordId;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, BorrowingRecordId>{
	
	List<BorrowingRecord> findAllByIdBookIdAndReturnDate(Integer bookId, LocalDate returnDate);
}

