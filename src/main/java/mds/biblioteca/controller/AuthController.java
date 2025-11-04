package mds.biblioteca.controller;

import mds.biblioteca.security.JwtTokenProvider;
import mds.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

record LoginRequest(@NotBlank String username, @NotBlank String password) {}
record RegisterRequest(
    @NotBlank @Size(min = 3, max = 20) String username,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 6, max = 40) String password
) {}
record JwtAuthenticationResponse(String token) {}

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired 
    UsuarioService usuarioService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            usuarioService.registrarUsuario(
                registerRequest.username(), 
                registerRequest.email(), 
                registerRequest.password()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(Map.of("mensaje", "Usuario registrado exitosamente."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("error", e.getMessage()));
        }
    }
}