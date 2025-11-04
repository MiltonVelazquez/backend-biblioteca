package mds.biblioteca.controller;

import mds.biblioteca.model.Libro;
import mds.biblioteca.service.LibroService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
class LibroDto{ 
    Long idLibro;
    String titulo; 
    String autor; 
    String isbn;
    String estado;
}

@Data
@NoArgsConstructor
class LibroCreateUpdateDto{ 
    @NotBlank String titulo;
    @NotBlank String autor;
    @NotBlank String isbn;
    @NotBlank String estado;
}

@RestController
@RequestMapping("/api/v1/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;
    
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/buscar")
    public ResponseEntity<List<LibroDto>> buscarLibros(
            @RequestParam(required = false) String titulo) {
        
        List<Libro> libros = libroService.buscarLibrosDisponibles(titulo);
        
        List<LibroDto> librosDto = libros.stream()
                .map(libro -> modelMapper.map(libro, LibroDto.class))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(librosDto);
    }
    
    @PostMapping
    public ResponseEntity<LibroDto> createLibro(@Valid @RequestBody LibroCreateUpdateDto libroDto) {
        Libro nuevoLibro = libroService.crearLibro(modelMapper.map(libroDto, Libro.class));
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(modelMapper.map(nuevoLibro, LibroDto.class));
    }
    @GetMapping("/{id}")
    public ResponseEntity<LibroDto> getLibroById(@PathVariable Long id) {
        Libro libro = libroService.obtenerPorId(id);
        return ResponseEntity.ok(modelMapper.map(libro, LibroDto.class));
    }

    @GetMapping
    public ResponseEntity<Page<LibroDto>> getAllLibros(Pageable pageable) {
        Page<Libro> paginaLibros = libroService.obtenerTodosPaginado(pageable);
        Page<LibroDto> paginaDto = paginaLibros.map(libro -> modelMapper.map(libro, LibroDto.class));
        return ResponseEntity.ok(paginaDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroDto> updateLibro(@PathVariable Long id, 
                                                @Valid @RequestBody LibroCreateUpdateDto libroDto) {
        Libro libroActualizado = libroService.actualizarLibro(id, modelMapper.map(libroDto, Libro.class));
        return ResponseEntity.ok(modelMapper.map(libroActualizado, LibroDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibro(@PathVariable Long id) {
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }
}
