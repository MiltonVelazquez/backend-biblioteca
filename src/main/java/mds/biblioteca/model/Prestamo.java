package mds.biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrestamo; // [cite: 22]
    
    private LocalDate fechaInicio; // [cite: 22]
    private LocalDate fechaFin; // [cite: 22]
    private LocalDate fechaDevolucion; // [cite: 22]

    // Relación (N,1) con Socio
    @ManyToOne
    @JoinColumn(name = "id_socio", referencedColumnName = "idSocio") // [cite: 22]
    private Socio socio;

    // Relación (N,1) con Libro
    @ManyToOne
    @JoinColumn(name = "id_libro", referencedColumnName = "idLibro") // [cite: 22]
    private Libro libro;

    // Relación (1,1) con Multa [cite: 15]
    @OneToOne(mappedBy = "prestamo")
    private Multa multa;
}
