// src/main/java/com/utn/frre/biblioteca/repository/PrestamoRepository.java
package mds.biblioteca.repository;

import mds.biblioteca.model.Libro;
import mds.biblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    /**
     * Para el flujo "Devolución": "¿Prestamo existe?".
     * Busca un préstamo activo (Fecha_Devolucion es null) para un libro específico.
     * Así sabemos qué préstamo estamos cerrando.
     */
    Optional<Prestamo> findByLibroAndFechaDevolucionIsNull(Libro libro);
}