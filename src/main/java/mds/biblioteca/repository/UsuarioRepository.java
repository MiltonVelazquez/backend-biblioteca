// src/main/java/mds/biblioteca/repository/UsuarioRepository.java
package mds.biblioteca.repository;

import mds.biblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método para que Spring Security encuentre usuarios al hacer login
    Optional<Usuario> findByUsername(String username);

    // Método para la lógica de registro
    Boolean existsByUsername(String username);

    // Método para la lógica de registro
    Boolean existsByEmail(String email);
}