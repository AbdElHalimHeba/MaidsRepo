package cc.maids.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

import cc.maids.dto.PatronDto;
import cc.maids.entity.Book;
import cc.maids.entity.Patron;
import cc.maids.exception.NotFoundException;
import cc.maids.repository.PatronRepository;
import cc.maids.service.PatronService;

@SpringBootTest
public class PatronServiceImplTest {

	@Autowired
	private PatronService service;
	
	@MockBean
	private PatronRepository repository;
		
	@MockBean
	private RedisTemplate<String, PatronDto> redisTemplate;
	
	@Test
	public void findShouldReturnEmptyIfNoPatronsExist() {
		
		when(repository.findAll(PageRequest.of(1, 1)))
		.thenReturn(Page.empty());
		
		List<PatronDto> actual = service.find(1, 1);
		
		assertEquals(Collections.EMPTY_LIST, actual);
	}
	
	@Test
	public void findByIdWithBooksShouldThrowExceptionIfIdNotExists() {
		
		when(redisTemplate.hasKey(anyString())).thenReturn(false);
		when(repository.findByIdWithBooks(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class, 
				() -> service.findById(anyInt()));
	}
	
	@Test
	public void createShouldReturnPatronIfEmailNotExists() {
		
		PatronDto input = buildPatronDto();
		Patron expected = buildPatron();
		
		when(repository.existsByEmail(anyString())).thenReturn(false);
		when(repository.save(any())).thenReturn(expected);
		
		PatronDto actual = service.create(input);
		
		assertion(expected, actual);
	}
	
	@Test
	public void createShouldThrowExceptionIfEmailExists() {
		
		PatronDto input = buildPatronDto();
		
		when(repository.existsByEmail(anyString())).thenReturn(true);
		
		assertThrows(IllegalArgumentException.class, 
				() -> service.create(input));
	}
	
	@Test
	public void updateByIdShouldThrowExceptionIfIdNotExists() {
		
		PatronDto input = buildPatronDto();
		
		when(redisTemplate.hasKey(anyString())).thenReturn(false);
		when(repository.findByIdWithBooks(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class, 
				() -> service.updateById(anyInt(), input));
	}
	
	@Test
	public void deleteByIdShouldThrowExceptionIfIdNotExists() {
		
		when(redisTemplate.hasKey(anyString())).thenReturn(false);
		when(repository.findByIdWithBooks(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class, 
				() -> service.deleteById(anyInt()));
	}
	
	@Test
	public void deleteByIdShouldThrowExceptionIfBorrowingBook() {
		
		Patron expectedPatron = buildPatron();
		
		Book expectedBook = BookServiceImplTest.buildBook();
		expectedPatron.setBooks(List.of(expectedBook));
		
		when(redisTemplate.hasKey(anyString())).thenReturn(false);
		when(repository.findByIdWithBooks(anyInt())).thenReturn(Optional.of(expectedPatron));
		
		assertThrows(IllegalStateException.class, 
				() -> service.deleteById(anyInt()));
	}
	
	private void assertion(Patron expected, PatronDto actual) {
		
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getEmail(), actual.getEmail());
		assertEquals(expected.getMobile(), actual.getMobile());
	}
	
	protected static Patron buildPatron() {
		Patron patron = new Patron();
		
		patron.setName("name");
		patron.setEmail("email");
		patron.setMobile("mobile");
		
		return patron;
	}
	
	protected static PatronDto buildPatronDto() {
		PatronDto dto = new PatronDto();
		
		dto.setName("name");
		dto.setEmail("email");
		dto.setMobile("mobile");
		
		return dto;
	}
	
}
