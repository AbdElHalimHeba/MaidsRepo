package cc.maids.dto;

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
public class PatronDto {
	
	@NotNull
	private String name;
	
	@NotNull
	private String email;
	
	@NotNull
	private String mobile;
	
}