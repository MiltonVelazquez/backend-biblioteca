// src/main/java/com/utn/frre/biblioteca/repository/PrestamoRepository.java
package mds.biblioteca.repository;

import mds.biblioteca.model.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import mds.biblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    Optional<Prestamo> findByLibroAndFechaDevolucionIsNull(Libro libro);

    Page<Prestamo> findByFechaDevolucionIsNotNull(Pageable pageable);
}
