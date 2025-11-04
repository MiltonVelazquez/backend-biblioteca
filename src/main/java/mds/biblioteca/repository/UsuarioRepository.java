// src/main/java/mds/biblioteca/repository/UsuarioRepository.java
package mds.biblioteca.repository;

import mds.biblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}