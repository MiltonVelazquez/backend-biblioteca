package mds.biblioteca.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MultaDTO{ 
    Long idMulta;
    Double monto;
    Long idPrestamo;
    String socioNombreCompleto;
    String socioNroSocio;
    boolean pagada;
    LocalDate fechaGeneracion;
}
