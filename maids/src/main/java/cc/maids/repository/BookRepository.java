package cc.maids.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cc.maids.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer>{
	
	boolean existsByIsbn(String isbn);
	
	@Query("SELECT b FROM Book b JOIN FETCH b.patron WHERE b.id = :id")
	Optional<Book> findByIdWithPatron(@Param("id") Integer id);
}
