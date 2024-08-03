package cc.maids.dto;

import java.time.LocalDate;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
@Validated
public class BookDto {
	
	@NotNull
	private String title;
	
	@NotNull
	private String author;
	
	private LocalDate publicationYear;
	
	@NotNull
	private String isbn;
}