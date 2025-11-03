package mds.biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

// --- ¡AÑADE ESTAS DOS IMPORTACIONES! ---
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@NoArgsConstructor
// --- ¡AÑADE ESTA ANOTACIÓN! ---
// 1. Intercepta todos los "DELETE" y los convierte en "UPDATE"
@SQLDelete(sql = "UPDATE libro SET activo = false WHERE id_libro = ?")
// --- ¡AÑADE ESTA ANOTACIÓN! ---
// 2. Hace que CADA consulta (findAll, findById, etc.) 
//    solo traiga los que tengan "activo = true".
@Where(clause = "activo = true")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLibro;
    
    private String titulo;
    private String autor;
    private String isbn;
    private String estado;

    // --- ¡AÑADE ESTE NUEVO CAMPO! ---
    // 3. La nueva columna. Por defecto, todos los libros nuevos serán 'true'.
    private boolean activo = true;

    @OneToMany(mappedBy = "libro")
    private Set<Prestamo> prestamos;
}
