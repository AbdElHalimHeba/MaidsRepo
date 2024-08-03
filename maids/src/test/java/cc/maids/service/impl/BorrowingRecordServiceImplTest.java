package cc.maids.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import cc.maids.entity.Book;
import cc.maids.entity.Patron;
import cc.maids.exception.NotFoundException;
import cc.maids.repository.BookRepository;
import cc.maids.repository.BorrowingRecordRepository;
import cc.maids.repository.PatronRepository;

@SpringBootTest
public class BorrowingRecordServiceImplTest {

	@InjectMocks
	private BorrowingRecordServiceImpl service;
	
	@Mock
	private BookRepository bookRepository;
	
	@Mock
	private PatronRepository patronRepository;
	
	@Mock
	private BorrowingRecordRepository repository;
		
	@Test
	public void borrowShouldThrowExceptionIfBookNotExists() {
		
		when(bookRepository.findById(anyInt()))
		.thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class, 
				() -> service.borrowBook(1, anyInt()));
	}
	
	@Test
	public void borrowShouldThrowExceptionIfIsBorrowed() {
		
		Patron patron = buildPatron();
		
		Book expected = buildBook();
		expected.setPatron(patron);
		
		when(bookRepository.findById(anyInt()))
		.thenReturn(Optional.of(expected));
		
		assertThrows(IllegalArgumentException.class, 
				() -> service.borrowBook(anyInt(), 2));
	}
	
	@Test
	public void borrowShouldThrowExceptionIfPatronNotExists() {
		
		Book expected = buildBook();
		
		when(bookRepository.findById(anyInt()))
		.thenReturn(Optional.of(expected));
		
		when(patronRepository.findById(anyInt()))
		.thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class, 
				() -> service.borrowBook(1, 1));
	}
	
	@Test
	public void returnShouldThrowExceptionIfBookNotExists() {
		
		when(bookRepository.findByIdWithPatron(anyInt()))
		.thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class, 
				() -> service.returnBook(1, anyInt()));
	}
	
	@Test
	public void returnShouldThrowExceptionIfIsNotBorrowed() {
		
		Book expected = buildBook();
		
		when(bookRepository.findByIdWithPatron(anyInt()))
		.thenReturn(Optional.of(expected));
		
		assertThrows(IllegalArgumentException.class, 
				() -> service.returnBook(1, anyInt()));
	}
	
	private Book buildBook() {
		Book book = new Book();
		
		book.setTitle("title");
		book.setAuthor("author");
		book.setPublicationYear(LocalDate.now());
		book.setIsbn("isbn");
		
		return book;
	}
	
	private Patron buildPatron() {
		Patron patron = new Patron();
		
		patron.setId(1);
		patron.setName("name");
		patron.setEmail("email");
		patron.setMobile("mobile");
		
		return patron;
	}
}
