package cc.maids.mapping;

import cc.maids.dto.PatronDto;
import cc.maids.entity.Patron;

public class PatronMapper {

	public static PatronDto patronToPatronDto(Patron patron) {
		PatronDto dto = new PatronDto();
		
		dto.setName(patron.getName());
		dto.setEmail(patron.getEmail());
		dto.setMobile(patron.getMobile());
		
		return dto;
	}
	
	public static Patron patronDtoToPatron(PatronDto dto) {
		Patron patron = new Patron();
		
		patron.setName(dto.getName());
		patron.setEmail(dto.getEmail());
		patron.setMobile(dto.getMobile());
		
		return patron;
	}
}
