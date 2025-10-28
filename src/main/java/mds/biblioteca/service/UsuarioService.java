// src/main/java/mds/biblioteca/service/UsuarioService.java
package mds.biblioteca.service;

import mds.biblioteca.model.Usuario;
import mds.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario (bibliotecario) en el sistema.
     * Verifica que el nombre de usuario o email no estén ya en uso.
     */
    @Transactional
    public Usuario registrarUsuario(String username, String email, String password) {
        
        // 1. Verificar si el usuario ya existe
        if (usuarioRepository.existsByUsername(username)) {
            throw new RuntimeException("Error: El nombre de usuario ya está en uso.");
        }

        // 2. Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("Error: El email ya está registrado.");
        }

        // 3. Crear el nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setEmail(email);
        
        // 4. Hashear la contraseña antes de guardarla
        usuario.setPassword(passwordEncoder.encode(password));

        // 5. Guardar en la base de datos
        return usuarioRepository.save(usuario);
    }
    
    // Aquí también podrías añadir un servicio para cargar usuarios por username
    // para Spring Security (UserDetailsService)
}