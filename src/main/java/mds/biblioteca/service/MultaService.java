package mds.biblioteca.service;

import mds.biblioteca.dto.MultaUpdateDto;
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

    private static final Double MONTO_MULTA_POR_DANIO = 5000.0;

    @Transactional
    public Multa crearMultaPorDevolucion(Prestamo prestamo) {
        Multa multa = new Multa();
        multa.setPrestamo(prestamo);
        multa.setSocio(prestamo.getSocio()); 
        multa.setMonto(MONTO_MULTA_POR_DANIO); 
        multa.setFechaGeneracion(LocalDate.now());
        multa.setPagada(false);
        
        Multa multaGuardada = multaRepository.save(multa);
        
        System.out.println("NOTIFICACIÓN: Se ha registrado una multa al socio " + prestamo.getSocio().getNombre());
        
        return multaGuardada;
    }

    @Transactional(readOnly = true)
    public List<Multa> obtenerMultasPendientes(String nroSocio) {
        Socio socio = socioRepository.findByNroSocio(nroSocio)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado: " + nroSocio));
        
        return multaRepository.findBySocioAndPagadaIsFalse(socio);
    }
    
    @Transactional
    public void pagarMulta(Long idMulta) {
        Multa multa = obtenerPorId(idMulta);
        if (multa.isPagada()) {
            throw new RuntimeException("La multa ya se encontraba pagada.");
        }
        multa.setPagada(true);
        multaRepository.save(multa);
    }

    @Transactional(readOnly = true)
    public Multa obtenerPorId(Long id) {
        return multaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Multa no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Multa> obtenerTodas() {
        return multaRepository.findAll();
    }

    @Transactional
    public Multa actualizarMonto(Long idMulta, MultaUpdateDto dto) {
        Multa multa = obtenerPorId(idMulta); 
        
        if (multa.isPagada()) {
            throw new RuntimeException("No se puede modificar una multa que ya ha sido pagada.");
        }

        multa.setMonto(dto.getMonto());
        return multaRepository.save(multa);
    }
}
