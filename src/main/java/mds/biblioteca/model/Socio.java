package mds.biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Data
public class Socio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSocio; 
    private String nombre; 
    private String apellido; 
    @Column(unique = true)
    private String nroSocio;
    @Column(unique = true)
    private String dni; 
    
    @OneToMany(mappedBy = "socio")
    private Set<Prestamo> prestamos;

    @OneToMany(mappedBy = "socio")
    private Set<Multa> multas;
}
