// src/main/java/mds/biblioteca/service/PrestamoService.java
package mds.biblioteca.service;

import mds.biblioteca.model.Libro;
import mds.biblioteca.model.Prestamo;
import mds.biblioteca.model.Socio;
import mds.biblioteca.repository.LibroRepository;
import mds.biblioteca.repository.PrestamoRepository;
import mds.biblioteca.repository.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private MultaService multaService; // Para verificar y crear multas

    private static final int DIAS_PRESTAMO = 15;

    /**
     * Implementa el flujo "Préstamo"[cite: 42].
     */
    @Transactional
    public Prestamo registrarPrestamo(Long idLibro, String nroSocio) {
        
        // 1. "Verificar socio válido" [cite: 71]
        Socio socio = socioRepository.findByNroSocio(nroSocio)
                .orElseThrow(() -> new RuntimeException("Socio no válido con Nro: " + nroSocio));

        // 2. "¿Multas pendientes?" [cite: 76]
        if (!multaService.obtenerMultasPendientes(nroSocio).isEmpty()) {
            // "Informar que debe pagar" [cite: 78]
            throw new RuntimeException("El socio tiene multas pendientes y no puede retirar libros.");
        }

        // 3. "¿Libro existe?" y "¿Hay edición?" (verificando estado) [cite: 49, 55]
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + idLibro));
        
        if (!libro.getEstado().equalsIgnoreCase("Disponible")) {
            // "Informar que no hay edición" (o no está disponible) [cite: 70]
            throw new RuntimeException("El libro no se encuentra disponible para préstamo.");
        }

        // 4. "Registrar préstamo" [cite: 79]
        Prestamo prestamo = new Prestamo();
        prestamo.setSocio(socio);
        prestamo.setLibro(libro);
        prestamo.setFechaInicio(LocalDate.now());
        prestamo.setFechaFin(LocalDate.now().plusDays(DIAS_PRESTAMO));
        prestamo.setFechaDevolucion(null); // Aún no se ha devuelto

        // 5. "Entregar libro" (actualizar estado del libro) [cite: 81]
        libro.setEstado("Prestado");
        libroRepository.save(libro);

        return prestamoRepository.save(prestamo);
    }

    /**
     * Implementa el flujo "Devolución"[cite: 59].
     */
    @Transactional
    public void registrarDevolucion(Long idLibro, String nroSocio, boolean buenasCondiciones) {
        
        // 1. "Socio entrega libro" - Encontrar el libro [cite: 60]
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + idLibro));

        // 2. "¿Prestamo existe?" (Buscamos un préstamo ACTIVO para este libro) [cite: 65]
        Prestamo prestamo = prestamoRepository.findByLibroAndFechaDevolucionIsNull(libro)
                .orElseThrow(() -> new RuntimeException("No se encontró un préstamo activo para este libro."));

        // 3. "Registrar devolución" (marcar la fecha) [cite: 67]
        prestamo.setFechaDevolucion(LocalDate.now());
        
        // 4. "Revisar estado físico" y "¿Libro en buenas condiciones?" [cite: 82, 83]
        if (buenasCondiciones) {
            // "Si" -> "Confirmar devolución" [cite: 72, 73]
            libro.setEstado("Disponible");
        } else {
            // "No" -> "Registrar multa" [cite: 84, 85]
            libro.setEstado("En Reparacion"); // El libro no puede volver a "Disponible"
            multaService.crearMultaPorDevolucion(prestamo);
            // "Notificar al socio" (se maneja dentro de multaService) [cite: 86]
        }

        // Guardar los cambios en la BBDD
        libroRepository.save(libro);
        prestamoRepository.save(prestamo);
    }
}