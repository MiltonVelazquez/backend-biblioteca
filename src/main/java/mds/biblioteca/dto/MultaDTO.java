package mds.biblioteca.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MultaDTO{ 
    Long IDMulta;
    Double Monto;
    Long Id_Prestamo;
    String socioNombreCompleto;
    String socioNroSocio;
    boolean pagada;
    LocalDate fechaGeneracion;
}