// src/main/java/mds/biblioteca/repository/MultaRepository.java
package mds.biblioteca.repository;

import mds.biblioteca.model.Multa;
import mds.biblioteca.model.Socio;
import org.springframework.data.jpa.repository.JpaRepository; // [cite: 134]
import java.util.List;

public interface MultaRepository extends JpaRepository<Multa, Long> {

    /**
     * Método de consulta personalizado para encontrar multas no pagadas de un socio.
     * Esencial para la lógica "¿Multas pendientes?". 
     */
    List<Multa> findBySocioAndPagadaIsFalse(Socio socio);
    
    // (Este método se hereda de la respuesta anterior)
    List<Multa> findBySocio(Socio socio);
}