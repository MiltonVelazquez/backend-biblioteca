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

    @Transactional
    public Usuario registrarUsuario(String username, String email, String password) {
        
        if (usuarioRepository.existsByUsername(username)) {
            throw new RuntimeException("Error: El nombre de usuario ya está en uso.");
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("Error: El email ya está registrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setEmail(email);
        
        usuario.setPassword(passwordEncoder.encode(password));

        return usuarioRepository.save(usuario);
    }
    
}