// src/main/java/mds/biblioteca/service/SocioService.java
package mds.biblioteca.service;

import mds.biblioteca.model.Socio;
import mds.biblioteca.repository.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SocioService {

    @Autowired
    private SocioRepository socioRepository;

    /**
     * Implementa el flujo "Dar de alta un socio".
     * Verifica el DNI y genera un Nro de Socio.
     */
    @Transactional
    public Socio registrarSocio(Socio socioData) {
        // Lógica de "¿Existe DNI?" [cite: 45]
        Optional<Socio> existente = socioRepository.findByDni(socioData.getDni());
        if (existente.isPresent()) {
            // "Rechazar inscripción" [cite: 51]
            throw new RuntimeException("El DNI ya se encuentra registrado.");
        }
        
        // "Generar número de socio" [cite: 69]
        socioData.setNroSocio(generarNumeroSocio()); 
        
        // "Registrar datos" [cite: 52]
        return socioRepository.save(socioData);
    }
    
    /**
     * Implementa el flujo "Verificar DNI en sistema".
     */
    @Transactional(readOnly = true)
    public boolean verificarDniExistente(String dni) {
        return socioRepository.findByDni(dni).isPresent();
    }
    
    /**
     * Genera un Nro de Socio único.
     */
    private String generarNumeroSocio() {
        // Implementación simple para un número único
        return "SOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // --- MÉTODOS REQUERIDOS POR EL CRUD DEL CONTROLLER ---

    /**
     * Obtiene un socio por su ID (PK).
     */
    @Transactional(readOnly = true)
    public Socio obtenerPorId(Long id) {
        return socioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con ID: " + id));
    }
    
    /**
     * Obtiene un socio por su Nro_Socio.
     * Implementa la lógica de "Verificar socio válido"[cite: 71].
     */
    @Transactional(readOnly = true)
    public Socio obtenerPorNroSocio(String nroSocio) {
        return socioRepository.findByNroSocio(nroSocio)
                .orElseThrow(() -> new RuntimeException("Socio no válido con Nro: " + nroSocio));
    }
    
    /**
     * Obtiene un listado de todos los socios.
     */
    @Transactional(readOnly = true)
    public List<Socio> obtenerTodos() {
        return socioRepository.findAll();
    }

    /**
     * Actualiza los datos de un socio existente.
     */
    @Transactional
    public Socio actualizarSocio(Long id, String nombre, String apellido) {
        Socio socio = obtenerPorId(id); // Reutiliza la lógica para encontrarlo
        socio.setNombre(nombre);
        socio.setApellido(apellido);
        return socioRepository.save(socio);
    }
    
    /**
     * Elimina un socio por su ID.
     */
    @Transactional
    public void eliminarSocio(Long id) {
        Socio socio = obtenerPorId(id);
        // Aquí podrías agregar lógica para verificar si tiene préstamos activos
        socioRepository.delete(socio);
    }
}