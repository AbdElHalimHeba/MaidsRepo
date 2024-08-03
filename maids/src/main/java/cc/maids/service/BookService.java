package cc.maids.service;

import java.util.List;

import cc.maids.dto.BookDto;

public interface BookService {

    public List<BookDto> find(int page, int size);
	
    public BookDto findById(Integer id);
	
    public BookDto create(BookDto dto);
	
    public BookDto updateById(Integer id, BookDto dto);
	
    public void deleteById(Integer id);
}
