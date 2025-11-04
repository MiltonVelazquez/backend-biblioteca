package mds.biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@NoArgsConstructor
@SQLDelete(sql = "UPDATE libro SET activo = false WHERE id_libro = ?") // al eliminar un libro lo desactiva
@Where(clause = "activo = true") // esto es para que se listen solo los libros activos
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
