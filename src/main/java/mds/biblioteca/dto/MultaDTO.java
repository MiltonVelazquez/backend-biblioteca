package mds.biblioteca.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MultaDTO{ 
    private Long idMulta;
    private Double monto;
    private Long idPrestamo;
    private String socioNombreCompleto;
    private String socioNroSocio;
    private boolean pagada;
    private LocalDate fechaGeneracion;
}
