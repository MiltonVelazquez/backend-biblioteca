package mds.biblioteca.controller;

import mds.biblioteca.dto.MultaDTO; 
import mds.biblioteca.model.Multa;
import mds.biblioteca.service.MultaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mds.biblioteca.dto.MultaUpdateDto; 
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/multas")
public class MultaController {
    @Autowired
    private MultaService multaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/pendientes/socio/{nroSocio}")
    public ResponseEntity<List<MultaDTO>> getMultasPendientesPorSocio(@PathVariable String nroSocio) {
        List<Multa> multas = multaService.obtenerMultasPendientes(nroSocio);
        return ResponseEntity.ok(multas.stream()
                .map(this::convertirADto) 
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
    public ResponseEntity<List<MultaDTO>> getAllMultas() {
        List<Multa> multas = multaService.obtenerTodas();
        return ResponseEntity.ok(multas.stream()
                .map(this::convertirADto) 
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MultaDTO> getMultaById(@PathVariable Long id) {
        Multa multa = multaService.obtenerPorId(id);
        return ResponseEntity.ok(convertirADto(multa)); 
    }

    private MultaDTO convertirADto(Multa multa) {
        MultaDTO dto = modelMapper.map(multa, MultaDTO.class);

        if (multa.getSocio() != null) {
            dto.setSocioNombreCompleto(multa.getSocio().getNombre() + " " + multa.getSocio().getApellido());
            dto.setSocioNroSocio(multa.getSocio().getNroSocio());
        }
        return dto;
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMontoMulta(@PathVariable Long id, @Valid @RequestBody MultaUpdateDto multaDto) {
        try {
            Multa multaActualizada = multaService.actualizarMonto(id, multaDto);
            return ResponseEntity.ok(convertirADto(multaActualizada)); // Asumiendo que tu helper se llama convertirADto
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("error", e.getMessage()));
        }
    }
}
