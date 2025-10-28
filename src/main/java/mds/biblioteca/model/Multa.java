// src/main/java/mds/biblioteca/model/Multa.java
package mds.biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data // [cite: 144]
public class Multa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMulta; // [cite: 13]
    
    private Double monto; // [cite: 14]
    
    // --- Campos Requeridos por la Lógica de Negocio ---
    
    private boolean pagada; // Para saber si está "pendiente" 
    
    private LocalDate fechaGeneracion; // Para registro

    // --- Relaciones del Diagrama ---
    
    @ManyToOne
    @JoinColumn(name = "Id_Socio", referencedColumnName = "IDSocio") // [cite: 14]
    private Socio socio;

    @OneToOne
    @JoinColumn(name = "Id_Prestamo", referencedColumnName = "IDPrestamo") // [cite: 14, 15]
    private Prestamo prestamo;
}
