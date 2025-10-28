// src/main/java/mds/biblioteca/controller/MultaController.java
package mds.biblioteca.controller;

// --- IMPORTS CORRECTOS ---
import mds.biblioteca.dto.MultaDto; // Desde el paquete dto
import mds.biblioteca.model.Multa;
import mds.biblioteca.service.MultaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// --- NO DEFINIR DTOS AQUÍ ---

@RestController
@RequestMapping("/api/v1/multas")
public class MultaController {
    @Autowired
    private MultaService multaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/pendientes/socio/{nroSocio}")
    public ResponseEntity<List<MultaDto>> getMultasPendientesPorSocio(@PathVariable String nroSocio) {
        List<Multa> multas = multaService.obtenerMultasPendientes(nroSocio);
        return ResponseEntity.ok(multas.stream()
                .map(this::convertirADto) // Usa el helper corregido
                .collect(Collectors.toList()));
    }

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

    @GetMapping
    public ResponseEntity<List<MultaDto>> getAllMultas() {
        List<Multa> multas = multaService.obtenerTodas();
        return ResponseEntity.ok(multas.stream()
                .map(this::convertirADto) // Usa el helper corregido
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MultaDto> getMultaById(@PathVariable Long id) {
        Multa multa = multaService.obtenerPorId(id);
        return ResponseEntity.ok(convertirADto(multa)); // Usa el helper corregido
    }

    // --- HELPER CORREGIDO ---
    private MultaDto convertirADto(Multa multa) {
        MultaDto dto = modelMapper.map(multa, MultaDto.class);

        if (multa.getSocio() != null) {
            dto.setSocioNombreCompleto(multa.getSocio().getNombre() + " " + multa.getSocio().getApellido());
            dto.setSocioNroSocio(multa.getSocio().getNroSocio());
        }
        // ModelMapper debería manejar idPrestamo si los nombres coinciden en camelCase
        return dto;
    }
}
