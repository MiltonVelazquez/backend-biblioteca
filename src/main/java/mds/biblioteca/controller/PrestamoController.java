// src/main/java/mds/biblioteca/controller/PrestamoController.java
package mds.biblioteca.controller;

// --- ¡IMPORTS ACTUALIZADOS! ---
// Ahora importamos todos los DTOs desde el paquete 'dto'
import mds.biblioteca.dto.PrestamoDto;
import mds.biblioteca.dto.PrestamoUpdateDto;
import mds.biblioteca.dto.PrestamoRequestDto;
import mds.biblioteca.dto.DevolucionRequestDto;

// --- Resto de imports necesarios ---
import mds.biblioteca.model.Prestamo;
import mds.biblioteca.service.PrestamoService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
// Ya no necesitas import java.time.LocalDate aquí

@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private ModelMapper modelMapper;

    // --- Endpoints (Sin cambios en la lógica interna) ---
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPrestamo(@Valid @RequestBody PrestamoRequestDto request) {
        try {
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
        }}
    // --- Método Helper (Corregido) ---
    private PrestamoDto convertirAPrestamoDto(Prestamo prestamo) {
        PrestamoDto dto = modelMapper.map(prestamo, PrestamoDto.class);
        
        // ¡Ahora sí llamará a los getters camelCase correctos!
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
