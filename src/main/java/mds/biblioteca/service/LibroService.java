// src/main/java/mds/biblioteca/service/LibroService.java
package mds.biblioteca.service;

import mds.biblioteca.model.Libro;
import mds.biblioteca.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    /**
     * Flujo "Préstamo" -> "Bibliotecario busca en sistema"[cite: 44].
     * Busca libros por título Y que estén disponibles.
     */
    @Transactional(readOnly = true)
    public List<Libro> buscarLibrosDisponibles(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            return libroRepository.findByEstado("Disponible");
        }
        return libroRepository.findByTituloContainingIgnoreCaseAndEstado(titulo, "Disponible");
    }

    // --- Métodos CRUD Estándar para LibroController ---

    @Transactional
    public Libro crearLibro(Libro libro) {
        // Asegurarse de que el estado inicial sea válido
        if (libro.getEstado() == null) {
            libro.setEstado("Disponible");
        }
        return libroRepository.save(libro);
    }

    @Transactional(readOnly = true)
    public Libro obtenerPorId(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Libro> obtenerTodosPaginado(Pageable pageable) {
        return libroRepository.findAll(pageable);
    }

    @Transactional
    public Libro actualizarLibro(Long id, Libro libroActualizado) {
        Libro libroExistente = obtenerPorId(id);
        
        libroExistente.setTitulo(libroActualizado.getTitulo());
        libroExistente.setAutor(libroActualizado.getAutor());
        libroExistente.setIsbn(libroActualizado.getIsbn());
        libroExistente.setEstado(libroActualizado.getEstado());
        
        return libroRepository.save(libroExistente);
    }

    @Transactional
    public void eliminarLibro(Long id) {
        Libro libro = obtenerPorId(id);
        // (Aquí se podría verificar que el libro no esté "Prestado")
        if (libro.getEstado().equalsIgnoreCase("Prestado")) {
            throw new RuntimeException("No se puede eliminar un libro que está actualmente prestado.");
        }
        libroRepository.delete(libro);
    }
}