package mds.biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data 
public class Multa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMulta; 
    
    private Double monto; 
    
    
    private boolean pagada; 
    
    private LocalDate fechaGeneracion; 

    
    
    @ManyToOne
    @JoinColumn(name = "id_socio", referencedColumnName = "idSocio") 
    private Socio socio;

    @OneToOne
    @JoinColumn(name = "id_prestamo", referencedColumnName = "idPrestamo") 
    private Prestamo prestamo;
}
