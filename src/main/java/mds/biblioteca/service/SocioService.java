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

    @Transactional
    public Socio registrarSocio(Socio socioData) {
        Optional<Socio> existente = socioRepository.findByDni(socioData.getDni());
        if (existente.isPresent()) {
            throw new RuntimeException("El DNI ya se encuentra registrado.");
        }
        
        socioData.setNroSocio(generarNumeroSocio()); 
        
        return socioRepository.save(socioData);
    }
    
    @Transactional(readOnly = true)
    public boolean verificarDniExistente(String dni) {
        return socioRepository.findByDni(dni).isPresent();
    }
    
    private String generarNumeroSocio() {
        return "SOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Transactional(readOnly = true)
    public Socio obtenerPorId(Long id) {
        return socioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con ID: " + id));
    }
    
    @Transactional(readOnly = true)
    public Socio obtenerPorNroSocio(String nroSocio) {
        return socioRepository.findByNroSocio(nroSocio)
                .orElseThrow(() -> new RuntimeException("Socio no válido con Nro: " + nroSocio));
    }
    
    @Transactional(readOnly = true)
    public List<Socio> obtenerTodos() {
        return socioRepository.findAll();
    }

    @Transactional
    public Socio actualizarSocio(Long id, String nombre, String apellido) {
        Socio socio = obtenerPorId(id); // Reutiliza la lógica para encontrarlo
        socio.setNombre(nombre);
        socio.setApellido(apellido);
        return socioRepository.save(socio);
    }

    @Transactional
    public void eliminarSocio(Long id) {
        Socio socio = obtenerPorId(id);
        socioRepository.delete(socio);
    }
}