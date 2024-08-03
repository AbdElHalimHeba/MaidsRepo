package cc.maids.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import cc.maids.dto.BookDto;
import cc.maids.entity.Book;
import cc.maids.exception.NotFoundException;
import cc.maids.mapping.BookMapper;
import cc.maids.repository.BookRepository;
import cc.maids.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	private static final String CACHE_KEY = "Book"; 
	
	@Autowired
	private BookRepository repository;
	
	@Autowired
	private RedisTemplate<String, BookDto> redisTemplate;
	
	@Override
	public List<BookDto> find(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		return repository.findAll(pageable)
				.stream()
                .map(BookMapper::bookToBookDto)
                .collect(Collectors.toList());
	}

	@Override
	public BookDto findById(Integer id) {
		if(redisTemplate.hasKey(CACHE_KEY + id)) return redisTemplate.opsForValue().get(CACHE_KEY + id);
		
		BookDto dto = repository.findById(id)
				.map(BookMapper::bookToBookDto)
				.orElseThrow(() -> new NotFoundException("Book with id " + id + " is not found"));
		
		redisTemplate.opsForValue().set(CACHE_KEY + id, dto);
		
		return dto;
	}

	@Override
	public BookDto create(BookDto dto) {
		if(repository.existsByIsbn(dto.getIsbn())) throw new IllegalArgumentException("Book with isbn " + dto.getIsbn() + " exists");
		
		Book book = repository.save(BookMapper.bookDtoToBook(dto));
		return BookMapper.bookToBookDto(book);

	}

	@Override
	public BookDto updateById(Integer id, BookDto dto) {
		if(redisTemplate.hasKey(CACHE_KEY + id)) redisTemplate.delete(CACHE_KEY + id);
		
		return repository.findById(id)
				 .map(book -> {
					 if(dto.getTitle() != null) book.setTitle(dto.getTitle());
					 if(dto.getAuthor() != null) book.setAuthor(dto.getAuthor());
					 if(dto.getPublicationYear() != null) book.setPublicationYear(dto.getPublicationYear());
					 if(dto.getIsbn() != null) book.setIsbn(dto.getIsbn());
					 
					 Book updated = repository.save(book);
					 return BookMapper.bookToBookDto(updated);
				 }).orElseThrow(() -> new NotFoundException("Book with id " + id + " is not found"));
	    
	}

	@Override
	public void deleteById(Integer id) {
		if(redisTemplate.hasKey(CACHE_KEY + id)) redisTemplate.delete(CACHE_KEY + id);
		
		Book book = repository.findById(id)
				.orElseThrow(() -> new NotFoundException("Book with id " + id + " is not found"));

		if(book.getPatron() != null) throw new IllegalStateException("Book with id " + id + " is borrowed and can not be deleted");
			
		repository.deleteById(id);

	}

}
