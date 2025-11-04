package mds.biblioteca.repository;

import mds.biblioteca.model.Multa;
import mds.biblioteca.model.Socio;
import org.springframework.data.jpa.repository.JpaRepository; 
import java.util.List;

public interface MultaRepository extends JpaRepository<Multa, Long> {

    List<Multa> findBySocioAndPagadaIsFalse(Socio socio);
    
    List<Multa> findBySocio(Socio socio);
}