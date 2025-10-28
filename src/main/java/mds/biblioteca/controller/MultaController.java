// src/main/java/com/utn/frre/biblioteca/controller/PrestamoController.java
package mds.biblioteca.controller;

import mds.biblioteca.model.Multa;
import mds.biblioteca.service.MultaService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@AllArgsConstructor
class MultaDTO{ 
    Long IDMulta;
    Double Monto;
    Long Id_Prestamo;
    String socioNombreCompleto;
    String socioNroSocio;
    boolean pagada;
    LocalDate fechaGeneracion;
}

@RestController
@RequestMapping("/api/v1/multas")
public class MultaController {
    @Autowired
    private MultaService multaService;

    @Autowired
    private ModelMapper modelMapper; // [cite: 148]

    /**
     * Implementa la verificación "¿Multas pendientes?".
     * Busca multas PENDIENTES (no pagadas) de un socio por su NroSocio.
     */
    @GetMapping("/pendientes/socio/{nroSocio}")
    public ResponseEntity<List<MultaDTO>> getMultasPendientesPorSocio(@PathVariable String nroSocio) {
        List<Multa> multas = multaService.obtenerMultasPendientes(nroSocio);
        return ResponseEntity.ok(multas.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList()));
    }

    /**
     * (CRUD Update) Marca una multa como pagada.
     * Esto es crucial para el flujo "Informar que debe pagar".
     */
    @PostMapping("/pagar/{id}")
    public ResponseEntity<?> pagarMulta(@PathVariable Long id) {
        try {
            multaService.pagarMulta(id);
            return ResponseEntity.ok(Map.of("mensaje", "Multa pagada exitosamente."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * (CRUD Read) Obtener todas las multas (para administración).
     */
    @GetMapping
    public ResponseEntity<List<MultaDTO>> getAllMultas() {
        List<Multa> multas = multaService.obtenerTodas();
        return ResponseEntity.ok(multas.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList()));
    }

    /**
     * (CRUD Read) Obtener una multa específica por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MultaDTO> getMultaById(@PathVariable Long id) {
        Multa multa = multaService.obtenerPorId(id);
        return ResponseEntity.ok(convertirADto(multa));
    }

    /**
     * Método helper para mapear la entidad Multa a su DTO,
     * incluyendo datos de las relaciones (Socio, Prestamo).
     */
    private MultaDTO convertirADto(Multa multa) {
        // ModelMapper simple
        MultaDTO dto = modelMapper.map(multa, MultaDTO.class); 
        
        // Mapeo manual de campos complejos
        return new MultaDTO(
            multa.getIDMulta(),
            multa.getMonto(),
            multa.getPrestamo() != null ? multa.getPrestamo().getIDPrestamo() : null,
            multa.getSocio() != null ? multa.getSocio().getNombre() + " " + multa.getSocio().getApellido() : "N/A",
            multa.getSocio() != null ? multa.getSocio().getNroSocio() : "N/A",
            false, // Aquí iría multa.isPagada()
            null   // Aquí iría multa.getFechaGeneracion()
        );
    }
}
