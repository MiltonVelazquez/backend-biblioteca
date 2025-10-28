// src/main/java/com/utn/frre/biblioteca/controller/PrestamoController.java
package mds.biblioteca.controller;

import mds.biblioteca.service.PrestamoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// DTOs para Préstamo y Devolución
@Data
@NoArgsConstructor
class PrestamoRequestDto{ 
    @NotNull Long idLibro;
    @NotBlank String nroSocio;
}
@Data
@NoArgsConstructor
class DevolucionRequestDto{ 
    @NotNull Long idLibro;
    @NotBlank String nroSocio;
    @NotNull Boolean buenasCondiciones;
}

@Data
@NoArgsConstructor
public class PrestamoDto {
    private Long idPrestamo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaDevolucion; // Será null si no se ha devuelto
    private Long idLibro;
    private String libroTitulo; // Para mostrar en el frontend
    private Long idSocio;
    private String socioNroSocio; // Para mostrar en el frontend
}

@Data
@NoArgsConstructor
public class PrestamoUpdateDto {
    // Es raro modificar estas fechas, pero posible para corregir errores.
    private LocalDate fechaInicio; 
    private LocalDate fechaFin;
    private LocalDate fechaDevolucion; // Podría usarse para corregir una devolución
}

@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    /**
     * Implementa el flujo "Préstamo".
     * Recibe "Socio solicita libro" [cite: 43] (ID de libro y Nro de socio).
     * El servicio interno se encarga de:
     * 1. "Verificar socio válido" [cite: 71]
     * 2. "¿Multas pendientes?" 
     * 3. "Registrar préstamo" [cite: 79]
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPrestamo(@Valid @RequestBody PrestamoRequestDto request) {
        try {
            prestamoService.registrarPrestamo(request.getIdLibro(), request.getNroSocio());
            // "Entregar libro" [cite: 81]
            return ResponseEntity.ok(Map.of("mensaje", "Préstamo registrado exitosamente."));
        
        } catch (RuntimeException e) {
            // "Informar que debe pagar" [cite: 78] o "Informar de libro no encontrado" [cite: 53]
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Implementa el flujo "Devolución".
     * Recibe "Socio entrega libro" [cite: 60] (ID de libro y si está en buenas condiciones).
     * El servicio interno se encarga de:
     * 1. "¿Prestamo existe?" [cite: 65]
     * 2. "Registrar devolución" [cite: 67]
     * 3. "Revisar estado físico" y "¿Libro en buenas condiciones?" [cite: 82, 83]
     * 4. "Registrar multa" si es necesario [cite: 85]
     */
    @PostMapping("/devolver")
    public ResponseEntity<?> registrarDevolucion(@Valid @RequestBody DevolucionRequestDto request) {
        try {
            prestamoService.registrarDevolucion(
                    request.getIdLibro(), 
                    request.getNroSocio(), 
                    request.getBuenasCondiciones()
            );
            // "Confirmar devolución" [cite: 73]
            return ResponseEntity.ok(Map.of("mensaje", "Devolución registrada."));
        
        } catch (RuntimeException e) {
            // "Informar error" [cite: 64] o "Notificar al socio" [cite: 86]
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("error", e.getMessage()));
        }
    }
}
