package mds.biblioteca.repository;

import mds.biblioteca.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    Optional<Libro> findByIsbn(String isbn);

    List<Libro> findByEstado(String estado);
    
    List<Libro> findByTituloContainingIgnoreCaseAndEstado(String titulo, String estado);
}