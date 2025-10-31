package mds.biblioteca.repository;

import mds.biblioteca.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    /**
     * Para el flujo "Préstamo": "Bibliotecario busca en sistema".
     * Permite buscar libros por título.
     */
    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    /**
     * Para el flujo "Préstamo": "Bibliotecario busca en sistema".
     * Permite buscar un libro exacto por su ISBN.
     */
    Optional<Libro> findByIsbn(String isbn);

    List<Libro> findByEstado(String estado);
    
    /**
     * Para el flujo "Préstamo": "¿Hay edición?"[cite: 55].
     * Podríamos usar esto para buscar libros por título Y que 
     * su estado sea "Disponible".
     */
    List<Libro> findByTituloContainingIgnoreCaseAndEstado(String titulo, String estado);
}