package cc.maids.mapping;

import java.time.LocalDate;

import cc.maids.entity.Book;
import cc.maids.entity.BorrowingRecord;
import cc.maids.entity.BorrowingRecordId;
import cc.maids.entity.Patron;

public class BorrowingRecordMapper {

	public static BorrowingRecord toBorrowingRecord(Book book, Patron patron, LocalDate borrowDate, LocalDate returnDate) {
		BorrowingRecord record = new BorrowingRecord();
		
		record.setId(new BorrowingRecordId(book.getId(), patron.getId(), borrowDate));
		record.setReturnDate(returnDate);
		
		return record;
	}	
}
