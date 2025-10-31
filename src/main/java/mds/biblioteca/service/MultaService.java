package mds.biblioteca.service;

import mds.biblioteca.model.Multa;
import mds.biblioteca.model.Prestamo;
import mds.biblioteca.model.Socio;
import mds.biblioteca.repository.MultaRepository;
import mds.biblioteca.repository.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MultaService {

    @Autowired
    private MultaRepository multaRepository;

    @Autowired
    private SocioRepository socioRepository;

    // Valor fijo para multas por daño
    private static final Double MONTO_MULTA_POR_DANIO = 50.0;

    /**
     * Implementa la lógica "Registrar multa" del flujo de Devolución. 
     * Se llama cuando un libro se devuelve en malas condiciones.
     */
    @Transactional
    public Multa crearMultaPorDevolucion(Prestamo prestamo) {
        Multa multa = new Multa();
        multa.setPrestamo(prestamo); // [cite: 14]
        multa.setSocio(prestamo.getSocio()); // [cite: 14]
        multa.setMonto(MONTO_MULTA_POR_DANIO); // [cite: 14]
        multa.setFechaGeneracion(LocalDate.now());
        multa.setPagada(false);
        
        Multa multaGuardada = multaRepository.save(multa);
        
        // Aquí iría la lógica de "Notificar al socio" [cite: 86]
        System.out.println("NOTIFICACIÓN: Se ha registrado una multa al socio " + prestamo.getSocio().getNombre());
        
        return multaGuardada;
    }

    /**
     * Implementa la lógica "¿Multas pendientes?" del flujo de Préstamo. 
     * Busca multas no pagadas para un socio específico.
     */
    @Transactional(readOnly = true)
    public List<Multa> obtenerMultasPendientes(String nroSocio) {
        Socio socio = socioRepository.findByNroSocio(nroSocio)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado: " + nroSocio));
        
        return multaRepository.findBySocioAndPagadaIsFalse(socio);
    }
    
    /**
     * Implementa la acción de pagar una multa, necesaria para 
     * limpiar el estado de "multas pendientes".
     */
    @Transactional
    public void pagarMulta(Long idMulta) {
        Multa multa = obtenerPorId(idMulta);
        if (multa.isPagada()) {
            throw new RuntimeException("La multa ya se encontraba pagada.");
        }
        multa.setPagada(true);
        multaRepository.save(multa);
    }

    // --- Métodos CRUD Estándar ---

    @Transactional(readOnly = true)
    public Multa obtenerPorId(Long id) {
        return multaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Multa no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Multa> obtenerTodas() {
        return multaRepository.findAll();
    }
}