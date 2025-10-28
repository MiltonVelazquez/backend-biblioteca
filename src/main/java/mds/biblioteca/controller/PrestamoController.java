// src/main/java/mds/biblioteca/controller/PrestamoController.java
package mds.biblioteca.controller;

// --- ¡IMPORTS NECESARIOS! ---
import mds.biblioteca.dto.PrestamoDto;             // Importar DTOs desde el paquete correcto
import mds.biblioteca.dto.PrestamoUpdateDto;
import mds.biblioteca.dto.PrestamoRequestDto;
import mds.biblioteca.dto.DevolucionRequestDto;
import mds.biblioteca.model.Prestamo;              // Importar el Modelo
import mds.biblioteca.service.PrestamoService;    // Importar el Servicio
import jakarta.validation.Valid;                  // Importar Valid
import org.modelmapper.ModelMapper;              // Importar ModelMapper
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;         // Importar Page
import org.springframework.data.domain.Pageable;      // Importar Pageable
import org.springframework.http.HttpStatus;         // Importar HttpStatus
import org.springframework.http.ResponseEntity;   // Importar ResponseEntity
import org.springframework.web.bind.annotation.*;     // Importar anotaciones Web

import java.time.LocalDate; // ¡IMPORTANTE PARA LOS DTOs SI ESTÁN AQUÍ, AUNQUE NO DEBERÍAN!
import java.util.Map;       // Importar Map

// --- ¡NO DEFINIR DTOS AQUÍ! ---
// Las definiciones de PrestamoRequestDto, DevolucionRequestDto, etc.
// deben estar en el paquete 'dto'.
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

    @Autowired
    private ModelMapper modelMapper; // Asegúrate de tener el @Bean de ModelMapper en tu config

    // --- Endpoints existentes (sin cambios en la lógica) ---
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPrestamo(@Valid @RequestBody PrestamoRequestDto request) {
        try {
            // Asumiendo que tus DTOs tienen getters correctos (generados por @Data)
            prestamoService.registrarPrestamo(request.getIdLibro(), request.getNroSocio());
            return ResponseEntity.ok(Map.of("mensaje", "Préstamo registrado exitosamente."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/devolver")
    public ResponseEntity<?> registrarDevolucion(@Valid @RequestBody DevolucionRequestDto request) {
        try {
             // Asumiendo getters correctos
            prestamoService.registrarDevolucion(
                    request.getIdLibro(), 
                    request.getNroSocio(), 
                    request.getBuenasCondiciones()
            );
            return ResponseEntity.ok(Map.of("mensaje", "Devolución registrada."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("error", e.getMessage()));
        }
    }

    // --- Endpoints NUEVOS (sin cambios en la lógica) ---
    @GetMapping
    public ResponseEntity<Page<PrestamoDto>> getAllPrestamos(Pageable pageable) {
        Page<Prestamo> paginaPrestamos = prestamoService.obtenerTodos(pageable);
        Page<PrestamoDto> paginaDto = paginaPrestamos.map(this::convertirAPrestamoDto);
        return ResponseEntity.ok(paginaDto);
    }

    @GetMapping("/devoluciones")
    public ResponseEntity<Page<PrestamoDto>> getAllDevoluciones(Pageable pageable) {
        Page<Prestamo> paginaDevoluciones = prestamoService.obtenerDevoluciones(pageable);
        Page<PrestamoDto> paginaDto = paginaDevoluciones.map(this::convertirAPrestamoDto);
        return ResponseEntity.ok(paginaDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrestamo(@PathVariable Long id, @Valid @RequestBody PrestamoUpdateDto prestamoUpdateDto) {
         try {
            Prestamo prestamoActualizado = prestamoService.actualizarPrestamo(id, prestamoUpdateDto);
            return ResponseEntity.ok(convertirAPrestamoDto(prestamoActualizado));
        } catch (RuntimeException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND) 
                                  .body(Map.of("error", e.getMessage()));
        }
    }

    // --- Método Helper (sin cambios en la lógica) ---
    private PrestamoDto convertirAPrestamoDto(Prestamo prestamo) {
        // Asegúrate de que tu PrestamoDto tenga los setters correctos (generados por @Data)
        PrestamoDto dto = modelMapper.map(prestamo, PrestamoDto.class);
        
        // Asumiendo que tus modelos Libro y Socio tienen getters correctos (generados por @Data)
        if (prestamo.getLibro() != null) {
            dto.setIdLibro(prestamo.getLibro().getIdLibro());
            dto.setLibroTitulo(prestamo.getLibro().getTitulo());
        }
        if (prestamo.getSocio() != null) {
            dto.setIdSocio(prestamo.getSocio().getIdSocio());
            dto.setSocioNroSocio(prestamo.getSocio().getNroSocio());
        }
        return dto;
    }
}
