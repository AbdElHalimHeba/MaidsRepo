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

import cc.maids.dto.PatronDto;
import cc.maids.service.PatronService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
public class PatronController {

	@Autowired
	private PatronService service;
	
	@GetMapping(path = "/api/patrons")
    public List<PatronDto> find(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
		return service.find(page, size);
	}
	
	@GetMapping(path = "/api/patrons/{id}")
    public PatronDto findById(@PathVariable @NotNull Integer id) {
		return service.findById(id);
	}
	
	@PostMapping(path = "/api/patrons")
    public PatronDto create(@Valid @RequestBody PatronDto dto) {
		return service.create(dto);
	}
	
	@PutMapping(path = "/api/patrons/{id}")
    public PatronDto updateById(@PathVariable @NotNull Integer id, @RequestBody PatronDto dto) {
		return service.updateById(id, dto);
	}
	
	@DeleteMapping(path = "/api/patrons/{id}")
    public void deleteById(@PathVariable @NotNull Integer id) {
		service.deleteById(id);
	}
}
