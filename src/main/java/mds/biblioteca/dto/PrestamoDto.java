package mds.biblioteca.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PrestamoDto {
    private Long idPrestamo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaDevolucion;
    private Long idLibro;
    private String libroTitulo;
    private Long idSocio;
    private String socioNroSocio;
}
