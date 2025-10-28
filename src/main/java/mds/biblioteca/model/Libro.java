package mds.biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Data
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLibro; // [cite: 27]
    private String titulo; // [cite: 28]
    private String autor; // [cite: 29]
    private String isbn; // [cite: 30]
    private String estado; // [cite: 31] (Ej: "Disponible", "Prestado")

    // Relación (N,1) con Prestamo [cite: 26]
    @OneToMany(mappedBy = "libro")
    private Set<Prestamo> prestamos;
}
