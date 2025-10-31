// src/main/java/com/utn/frre/biblioteca/controller/SocioController.java
package mds.biblioteca.controller;

import mds.biblioteca.model.Socio;
import mds.biblioteca.service.SocioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper; //
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// DTOs (Data Transfer Objects) para Socio
@Data
@NoArgsConstructor
class SocioAltaDto{
    @NotBlank String nombre;
    @NotBlank String apellido;
    @NotBlank @Pattern(regexp = "^[0-9]{7,8}$") String dni;
}
@Data
@NoArgsConstructor
class SocioUpdateDto{
    @NotBlank String nombre;
    @NotBlank String apellido;
}
@Data
@NoArgsConstructor
class SocioResponseDto{
    Long IDSocio;
    String nombre;
    String apellido;
    String nroSocio;
}

@RestController
@RequestMapping("/api/v1/socios")
public class SocioController {

    @Autowired
    private SocioService socioService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Implementa el flujo "Dar de alta un socio".
     * Recibe DNI, Nombre, Apellido [cite: 17-19].
     * Verifica DNI y registra[cite: 40, 52].
     */
    @PostMapping("/alta")
    public ResponseEntity<?> altaSocio(@Valid @RequestBody SocioAltaDto socioDto) {
        try {
            // Usar ModelMapper para convertir DTO a Entidad
            Socio nuevoSocio = modelMapper.map(socioDto, Socio.class);
            Socio socioGuardado = socioService.registrarSocio(nuevoSocio);
            
            // "Informar" y "Generar número de socio" [cite: 68, 69]
            SocioResponseDto responseDto = modelMapper.map(socioGuardado, SocioResponseDto.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        
        } catch (RuntimeException e) {
            // "Rechazar inscripción" [cite: 51] o "Informar error"
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Implementa el flujo "Verificar DNI en sistema"[cite: 40].
     */
    @GetMapping("/verificar-dni/{dni}")
    public ResponseEntity<?> verificarDni(@PathVariable String dni) {
        // "¿Existe DNI?" [cite: 45]
        boolean existe = socioService.verificarDniExistente(dni);
        return ResponseEntity.ok(Map.of("existe", existe));
    }
    
    /**
     * (CRUD Read) Obtener un socio por su ID de base de datos.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SocioResponseDto> getSocioById(@PathVariable Long id) {
        Socio socio = socioService.obtenerPorId(id);
        return ResponseEntity.ok(modelMapper.map(socio, SocioResponseDto.class));
    }

    /**
     * (CRUD Read) Obtener un socio por su Nro_Socio (flujo "Verificar socio válido").
     */
    @GetMapping("/nro/{nroSocio}")
    public ResponseEntity<SocioResponseDto> getSocioByNroSocio(@PathVariable String nroSocio) {
        Socio socio = socioService.obtenerPorNroSocio(nroSocio);
        return ResponseEntity.ok(modelMapper.map(socio, SocioResponseDto.class));
    }

    /**
     * (CRUD Read) Obtener todos los socios.
     */
    @GetMapping
    public ResponseEntity<List<SocioResponseDto>> getAllSocios() {
        List<Socio> socios = socioService.obtenerTodos();
        List<SocioResponseDto> dtos = socios.stream()
                .map(socio -> modelMapper.map(socio, SocioResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * (CRUD Update) Actualizar datos de un socio.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SocioResponseDto> updateSocio(@PathVariable Long id, 
                                                       @Valid @RequestBody SocioUpdateDto socioUpdateDto) {
        Socio socioActualizado = socioService.actualizarSocio(id, socioUpdateDto.getNombre(), socioUpdateDto.getApellido());
        return ResponseEntity.ok(modelMapper.map(socioActualizado, SocioResponseDto.class));
    }

    /**
     * (CRUD Delete) Eliminar (o desactivar) un socio.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSocio(@PathVariable Long id) {
        socioService.eliminarSocio(id);
        return ResponseEntity.noContent().build();
    }
}