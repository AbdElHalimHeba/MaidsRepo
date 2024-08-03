package cc.maids.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cc.maids.entity.Patron;

public interface PatronRepository extends JpaRepository<Patron, Integer> {
	
	boolean existsByEmail(String email);
	
	@Query("SELECT p FROM Patron p LEFT JOIN FETCH p.books WHERE p.id = :id")
    Optional<Patron> findByIdWithBooks(@Param("id") Integer id);
}
