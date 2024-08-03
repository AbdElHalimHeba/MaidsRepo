package cc.maids.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.maids.service.BorrowingRecordService;
import jakarta.validation.constraints.NotNull;

@RestController
public class BorrowingRecordController {

	@Autowired
	private BorrowingRecordService service;
	
	@PostMapping(path = "/api/borrow/{bookId}/patron/{patronId}")
    public void borrowBook(@PathVariable @NotNull Integer bookId, @PathVariable @NotNull Integer patronId) {
		service.borrowBook(bookId, patronId);
    }
	
	@PutMapping(path = "/api/return/{bookId}/patron/{patronId}")
    public void returnBook(@PathVariable @NotNull Integer bookId, @PathVariable @NotNull Integer patronId) {
		service.returnBook(bookId, patronId);
    }
}
