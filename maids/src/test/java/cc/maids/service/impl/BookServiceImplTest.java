package cc.maids.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;

import cc.maids.dto.BookDto;
import cc.maids.entity.Book;
import cc.maids.entity.Patron;
import cc.maids.exception.NotFoundException;
import cc.maids.repository.BookRepository;
import cc.maids.service.BookService;

@SpringBootTest
public class BookServiceImplTest {

	@Autowired
	private BookService service;
	
	@MockBean
	private BookRepository repository;
	
	@MockBean
	private RedisTemplate<String, BookDto> redisTemplate;
		
	@Test
	public void findShouldReturnEmptyIfNoBooksExist() {
		
		when(repository.findAll(PageRequest.of(1, 1))).thenReturn(Page.empty());
		
		List<BookDto> actual = service.find(1, 1);
		
		assertEquals(Collections.EMPTY_LIST, actual);
	}
	
	@Test
	public void findByIdShouldThrowExceptionIfIdNotExists() {
		
		when(redisTemplate.hasKey(anyString())).thenReturn(false);
		when(repository.findById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class, 
				() -> service.findById(anyInt()));
	}
	
	@Test
	public void createShouldReturnBookIfIsbnNotExists() {
		
		BookDto input = buildBookDto();
		Book expected = buildBook();
		
		when(repository.existsByIsbn(anyString())).thenReturn(false);
		when(repository.save(any())).thenReturn(expected);
		
		BookDto actual = service.create(input);
		
		assertion(expected, actual);
	}
	
	@Test
	public void createShouldThrowExceptionIfIsbnExists() {
		
		BookDto input = buildBookDto();
		
		when(repository.existsByIsbn(anyString())).thenReturn(true);
		
		assertThrows(IllegalArgumentException.class, 
				() -> service.create(input));
	}
	
	@Test
	public void updateByIdShouldThrowExceptionIfIdNotExists() {
		
		BookDto input = buildBookDto();
		
		when(redisTemplate.hasKey(anyString())).thenReturn(false);
		when(repository.findById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class, 
				() -> service.updateById(anyInt(), input));
	}
	
	@Test
	public void deleteByIdShouldThrowExceptionIfIdNotExists() {
		
		when(redisTemplate.hasKey(anyString())).thenReturn(false);
		when(repository.findById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class, 
				() -> service.deleteById(anyInt()));
	}
	
	@Test
	public void deleteByIdShouldThrowExceptionIfBorrowed() {
		
		Book expected = buildBook();
		expected.setPatron(new Patron());
		
		when(redisTemplate.hasKey(anyString())).thenReturn(false);
		when(repository.findById(anyInt())).thenReturn(Optional.of(expected));
		
		assertThrows(IllegalStateException.class, 
				() -> service.deleteById(anyInt()));
	}
	
	private void assertion(Book expected, BookDto actual) {
		
		assertEquals(expected.getTitle(), actual.getTitle());
		assertEquals(expected.getAuthor(), actual.getAuthor());
		assertEquals(expected.getPublicationYear(), actual.getPublicationYear());
		assertEquals(expected.getIsbn(), actual.getIsbn());
	}
	
	protected static Book buildBook() {
		Book book = new Book();
		
		book.setTitle("title");
		book.setAuthor("author");
		book.setPublicationYear(LocalDate.now());
		book.setIsbn("isbn");
		
		return book;
	}
	
	protected static BookDto buildBookDto() {
		BookDto dto = new BookDto();
		
		dto.setTitle("title");
		dto.setAuthor("author");
		dto.setPublicationYear(LocalDate.now());
		dto.setIsbn("isbn");
		
		return dto;
	}
	
}
