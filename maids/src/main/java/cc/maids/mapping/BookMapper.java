package cc.maids.mapping;

import cc.maids.dto.BookDto;
import cc.maids.entity.Book;

public class BookMapper {

	public static BookDto bookToBookDto(Book book) {
		BookDto dto = new BookDto();
		
		dto.setTitle(book.getTitle());
		dto.setAuthor(book.getAuthor());
		dto.setPublicationYear(book.getPublicationYear());
		dto.setIsbn(book.getIsbn());
		
		return dto;
	}
	
	public static Book bookDtoToBook(BookDto dto) {
		Book book = new Book();
		
		book.setTitle(dto.getTitle());
		book.setAuthor(dto.getAuthor());
		book.setPublicationYear(dto.getPublicationYear());
		book.setIsbn(dto.getIsbn());
		
		return book;
	}
}
