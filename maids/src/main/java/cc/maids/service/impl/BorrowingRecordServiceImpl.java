package cc.maids.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cc.maids.entity.Book;
import cc.maids.entity.BorrowingRecord;
import cc.maids.entity.Patron;
import cc.maids.exception.NotFoundException;
import cc.maids.mapping.BorrowingRecordMapper;
import cc.maids.repository.BookRepository;
import cc.maids.repository.BorrowingRecordRepository;
import cc.maids.repository.PatronRepository;
import cc.maids.service.BorrowingRecordService;

@Service
public class BorrowingRecordServiceImpl implements BorrowingRecordService {

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private PatronRepository patronRepository;
	
	@Autowired
	private BorrowingRecordRepository borrowingRepository;
	
	@Transactional
	@Override
	public void borrowBook(Integer bookId, Integer patronId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new NotFoundException("Book with id " + bookId + " is not found"));
		
		boolean isBorrowed = book.getPatron() != null;
		if(isBorrowed) throw new IllegalArgumentException("Book with id " + bookId + " is already borrowed");
		
		Patron patron = patronRepository.findById(patronId)
				.orElseThrow(() -> new NotFoundException("Patron with id " + patronId + " is not found"));
		
		BorrowingRecord record = BorrowingRecordMapper.toBorrowingRecord(book, patron, LocalDate.now(), null);
		borrowingRepository.save(record);
		
		book.setPatron(patron);
		bookRepository.save(book);
		
	}

	@Transactional
	@Override
	public void returnBook(Integer bookId, Integer patronId) {
		Book book = bookRepository.findByIdWithPatron(bookId)
				.orElseThrow(() -> new NotFoundException("Book with id " + bookId + " is not found"));
		
		boolean isBorrowed = book.getPatron() != null && book.getPatron().getId() == patronId;
		if(!isBorrowed) throw new IllegalArgumentException("Book with id " + bookId + " is not borrowed by patron " + patronId);
		
		borrowingRepository.findAllByIdBookIdAndReturnDate(book.getId(), null)
				.stream()
				.findFirst()
				.ifPresent(record -> {
					record.setReturnDate(LocalDate.now());
					borrowingRepository.save(record);
				});
		
		book.setPatron(null);
		bookRepository.save(book);
		
	}

}
