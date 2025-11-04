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

    @PostMapping("/alta")
    public ResponseEntity<?> altaSocio(@Valid @RequestBody SocioAltaDto socioDto) {
        try {
            Socio nuevoSocio = modelMapper.map(socioDto, Socio.class);
            Socio socioGuardado = socioService.registrarSocio(nuevoSocio);
            
            SocioResponseDto responseDto = modelMapper.map(socioGuardado, SocioResponseDto.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("error", e.getMessage()));
        }
    }

    
    @GetMapping("/verificar-dni/{dni}")
    public ResponseEntity<?> verificarDni(@PathVariable String dni) {

        boolean existe = socioService.verificarDniExistente(dni);
        return ResponseEntity.ok(Map.of("existe", existe));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SocioResponseDto> getSocioById(@PathVariable Long id) {
        Socio socio = socioService.obtenerPorId(id);
        return ResponseEntity.ok(modelMapper.map(socio, SocioResponseDto.class));
    }

    @GetMapping("/nro/{nroSocio}")
    public ResponseEntity<SocioResponseDto> getSocioByNroSocio(@PathVariable String nroSocio) {
        Socio socio = socioService.obtenerPorNroSocio(nroSocio);
        return ResponseEntity.ok(modelMapper.map(socio, SocioResponseDto.class));
    }

    @GetMapping
    public ResponseEntity<List<SocioResponseDto>> getAllSocios() {
        List<Socio> socios = socioService.obtenerTodos();
        List<SocioResponseDto> dtos = socios.stream()
                .map(socio -> modelMapper.map(socio, SocioResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocioResponseDto> updateSocio(@PathVariable Long id, 
                                                       @Valid @RequestBody SocioUpdateDto socioUpdateDto) {
        Socio socioActualizado = socioService.actualizarSocio(id, socioUpdateDto.getNombre(), socioUpdateDto.getApellido());
        return ResponseEntity.ok(modelMapper.map(socioActualizado, SocioResponseDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSocio(@PathVariable Long id) {
        socioService.eliminarSocio(id);
        return ResponseEntity.noContent().build();
    }
}