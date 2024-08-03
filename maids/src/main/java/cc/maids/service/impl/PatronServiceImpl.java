package cc.maids.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import cc.maids.dto.PatronDto;
import cc.maids.entity.Patron;
import cc.maids.exception.NotFoundException;
import cc.maids.mapping.PatronMapper;
import cc.maids.repository.PatronRepository;
import cc.maids.service.PatronService;

@Service
public class PatronServiceImpl implements PatronService {

	private static final String CACHE_KEY = "Patron"; 
	
	@Autowired
	private PatronRepository repository;
	
	@Autowired
	private RedisTemplate<String, PatronDto> redisTemplate;
	
	@Override
	public List<PatronDto> find(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		
		return repository.findAll(pageable)
				.stream()
                .map(PatronMapper::patronToPatronDto)
                .collect(Collectors.toList());
	}

	@Override
	public PatronDto findById(Integer id) {
		if(redisTemplate.hasKey(CACHE_KEY + id)) return redisTemplate.opsForValue().get(CACHE_KEY + id);
		
		PatronDto dto = repository.findById(id)
				.map(PatronMapper::patronToPatronDto)
				.orElseThrow(() -> new NotFoundException("Patron with id " + id + " is not found"));

		redisTemplate.opsForValue().set(CACHE_KEY + id, dto);
		
		return dto;
	}

	@Override
	public PatronDto create(PatronDto dto) {
		if(repository.existsByEmail(dto.getEmail())) throw new IllegalArgumentException("Patron with email " + dto.getEmail() + " exists");
		
		Patron patron = repository.save(PatronMapper.patronDtoToPatron(dto));
		return PatronMapper.patronToPatronDto(patron);

	}

	@Override
	public PatronDto updateById(Integer id, PatronDto dto) {
		if(redisTemplate.hasKey(CACHE_KEY + id)) redisTemplate.delete(CACHE_KEY + id);
		
		return repository.findById(id)
				 .map(patron -> {
					 if(dto.getName() != null) patron.setName(dto.getName());
					 if(dto.getEmail() != null) patron.setEmail(dto.getEmail());
					 if(dto.getMobile() != null) patron.setMobile(dto.getMobile());
					 
					 Patron updated = repository.save(patron);
					 return PatronMapper.patronToPatronDto(updated);
				 }).orElseThrow(() -> new NotFoundException("Patron with id " + id + " is not found"));
	    
	}

	@Override
	public void deleteById(Integer id) {
		if(redisTemplate.hasKey(CACHE_KEY + id)) redisTemplate.delete(CACHE_KEY + id);
		
		Patron patron = repository.findByIdWithBooks(id)
				.orElseThrow(() -> new NotFoundException("Patron with id " + id + " is not found"));
				
		if(!patron.getBooks().isEmpty()) throw new IllegalStateException("Patron with id " + id + " is borrowing a book and can not be deleted");
			
		repository.deleteById(id);

	}

}
