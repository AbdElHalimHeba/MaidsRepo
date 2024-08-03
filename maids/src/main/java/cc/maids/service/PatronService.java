package cc.maids.service;

import java.util.List;

import cc.maids.dto.PatronDto;

public interface PatronService {

	public List<PatronDto> find(int page, int size);
	
    public PatronDto findById(Integer id);
	
    public PatronDto create(PatronDto dto);
	
    public PatronDto updateById(Integer id, PatronDto dto);
	
    public void deleteById(Integer id);
}
