// src/main/java/mds/biblioteca/dto/PrestamoDtos.java
package mds.biblioteca.dto; // Asegúrate de que el paquete sea correcto

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * DTO para mostrar información detallada de un préstamo.
 * Usado para respuestas GET y PUT.
 */
@Data
@NoArgsConstructor
public class PrestamoDto { // Nota: 'class' es correcto, no necesita 'public' si está en el mismo archivo
    private Long idPrestamo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaDevolucion;
    private Long idLibro;
    private String libroTitulo;
    private Long idSocio;
    private String socioNroSocio;
}

/**
 * DTO para actualizar campos de un préstamo existente.
 * Usado para el body de PUT /prestamos/{id}.
 */
@Data
@NoArgsConstructor
public class PrestamoUpdateDto {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaDevolucion;
}

/**
 * DTO para registrar un nuevo préstamo.
 * Usado para el body de POST /prestamos/registrar.
 */
@Data
@NoArgsConstructor
public class PrestamoRequestDto {
    @NotNull private Long idLibro;
    @NotBlank private String nroSocio;
}

/**
 * DTO para registrar una devolución.
 * Usado para el body de POST /prestamos/devolver.
 */
@Data
@NoArgsConstructor
public class DevolucionRequestDto {
    @NotNull private Long idLibro;
    @NotBlank private String nroSocio;
    @NotNull private Boolean buenasCondiciones;
}

// Nota: Puedes hacer estas clases 'public' si prefieres,
// pero no es estrictamente necesario si solo se usan dentro
// del mismo paquete o por clases que importan el paquete.
// Dejarlas sin 'public' (package-private) es común para DTOs internos.
