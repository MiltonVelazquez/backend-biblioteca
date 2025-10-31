package mds.biblioteca.repository;

import mds.biblioteca.model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Long> {
    // Método para "Verificar DNI en sistema" [cite: 40]
    Optional<Socio> findByDni(String dni);
    
    // Método para "Verificar socio válido"
    Optional<Socio> findByNroSocio(String nroSocio);
}