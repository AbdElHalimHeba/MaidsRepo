package cc.maids.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.maids.dto.BookDto;
import cc.maids.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
public class BookController {

	@Autowired
	private BookService service;
	
	@GetMapping(path = "/api/books")
    public List<BookDto> find(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
		return service.find(page, size);
    }
	
	@GetMapping(path = "/api/books/{id}")
    public BookDto findById(@PathVariable @NotNull Integer id) {
		return service.findById(id);
    }
	
	@PostMapping(path = "/api/books")
    public BookDto create(@Valid @RequestBody BookDto dto) {
		return service.create(dto);
    }
	
	@PutMapping(path = "/api/books/{id}")
    public BookDto updateById(@PathVariable @NotNull Integer id, @RequestBody BookDto dto) {
		return service.updateById(id, dto);
    }
	
	@DeleteMapping(path = "/api/books/{id}")
    public void deleteById(@PathVariable @NotNull Integer id) {
		service.deleteById(id);
    }
}
