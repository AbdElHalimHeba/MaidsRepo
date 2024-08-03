package cc.maids.service;

public interface BorrowingRecordService {

    public void borrowBook(Integer bookId, Integer patronId);
	
    public void returnBook(Integer bookId, Integer patronId);
}
