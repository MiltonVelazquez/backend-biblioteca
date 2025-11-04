package mds.biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrestamo; 
    private LocalDate fechaInicio; 
    private LocalDate fechaFin;
    private LocalDate fechaDevolucion; 

   
    @ManyToOne
    @JoinColumn(name = "id_socio", referencedColumnName = "idSocio") 
    private Socio socio;

    @ManyToOne
    @JoinColumn(name = "id_libro", referencedColumnName = "idLibro") 
    private Libro libro;

    @OneToOne(mappedBy = "prestamo")
    private Multa multa;
}
