// src/main/java/mds/biblioteca/service/PrestamoService.java
package mds.biblioteca.service;

import mds.biblioteca.dto.PrestamoUpdateDto;

import mds.biblioteca.model.Libro;
import mds.biblioteca.model.Prestamo;
import mds.biblioteca.model.Socio;
import mds.biblioteca.repository.LibroRepository;
import mds.biblioteca.repository.PrestamoRepository;
import mds.biblioteca.repository.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import java.time.LocalDate;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private MultaService multaService; 

    private static final int DIAS_PRESTAMO = 15;

    @Transactional
    public Prestamo registrarPrestamo(Long idLibro, String nroSocio) {
        
        // 1. Verificar socio válido
        Socio socio = socioRepository.findByNroSocio(nroSocio)
                .orElseThrow(() -> new RuntimeException("Socio no válido con Nro: " + nroSocio));

        // 2. ¿Multas pendientes?
        if (!multaService.obtenerMultasPendientes(nroSocio).isEmpty()) {
            throw new RuntimeException("El socio tiene multas pendientes y no puede retirar libros.");
        }

        // 3. ¿Libro existe y está disponible?
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + idLibro));
        
        if (!libro.getEstado().equalsIgnoreCase("Disponible")) {
            throw new RuntimeException("El libro no se encuentra disponible para préstamo.");
        }
        
        // --- CÓDIGO DUPLICADO ELIMINADO ---

        // 4. Crear y guardar el préstamo
        Prestamo prestamo = new Prestamo();
        prestamo.setSocio(socio);
        prestamo.setLibro(libro);
        prestamo.setFechaInicio(LocalDate.now());
        prestamo.setFechaFin(LocalDate.now().plusDays(DIAS_PRESTAMO));
        prestamo.setFechaDevolucion(null); 
        
        // 5. Actualizar estado del libro
        libro.setEstado("Prestado");
        libroRepository.save(libro);
        
        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public void registrarDevolucion(Long idLibro, String nroSocio, boolean buenasCondiciones) {
        
        // 1. Encontrar el libro
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + idLibro));

        // 2. Encontrar el préstamo activo para ese libro
        Prestamo prestamo = prestamoRepository.findByLibroAndFechaDevolucionIsNull(libro)
                .orElseThrow(() -> new RuntimeException("No se encontró un préstamo activo para este libro."));

        // 3. Registrar fecha de devolución
        prestamo.setFechaDevolucion(LocalDate.now());
        
        // 4. Revisar estado y actualizar/multar
        if (buenasCondiciones) {
            libro.setEstado("Disponible");
        } else {
            libro.setEstado("En Reparacion"); 
            multaService.crearMultaPorDevolucion(prestamo);
        }
        
        // --- CÓDIGO DUPLICADO ELIMINADO ---

        // 5. Guardar cambios
        libroRepository.save(libro);
        prestamoRepository.save(prestamo);
    }

    // --- Métodos NUEVOS (Estos estaban bien) ---

    @Transactional(readOnly = true)
    public Page<Prestamo> obtenerTodos(Pageable pageable) {
        return prestamoRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Prestamo> obtenerDevoluciones(Pageable pageable) {
        return prestamoRepository.findByFechaDevolucionIsNotNull(pageable);
    }

    @Transactional
    public Prestamo actualizarPrestamo(Long idPrestamo, PrestamoUpdateDto dto) {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + idPrestamo));

        if (dto.getFechaInicio() != null) {
            prestamo.setFechaInicio(dto.getFechaInicio());
        }
        if (dto.getFechaFin() != null) {
            prestamo.setFechaFin(dto.getFechaFin());
        }
        if (dto.getFechaDevolucion() != null) {
            prestamo.setFechaDevolucion(dto.getFechaDevolucion());
        }

        return prestamoRepository.save(prestamo);
    }
}
