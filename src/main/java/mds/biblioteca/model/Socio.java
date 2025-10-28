package mds.biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Data // 
public class Socio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IDSocio; // [cite: 17]
    private String nombre; // [cite: 17]
    private String apellido; // [cite: 18]
    @Column(unique = true)
    private String nroSocio; // [cite: 18]
    @Column(unique = true)
    private String dni; // [cite: 19]
    
    // Relación (1,N) con Prestamo [cite: 20]
    @OneToMany(mappedBy = "socio")
    private Set<Prestamo> prestamos;

    // Relación (1,N) con Multa [cite: 11]
    @OneToMany(mappedBy = "socio")
    private Set<Multa> multas;
}
