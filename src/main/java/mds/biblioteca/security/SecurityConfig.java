// src/main/java/mds/biblioteca/security/SecurityConfig.java
package mds.biblioteca.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults; // <-- ¡Importante!

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // (Aquí va tu bean de JwtAuthenticationFilter si lo tienes)

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // ---- 1. AÑADE ESTA LÍNEA PARA ACTIVAR CORS ----
            .cors(withDefaults()) 
            
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // (Aquí van tus reglas de requestMatchers)
                .anyRequest().permitAll() // Permite todo (como lo tenías)
            );
            
        // (Aquí añades tu filtro JWT si lo tienes)

        return http.build();
    }

    // ---- 2. AÑADE ESTE BEAN COMPLETO ----
    // Este bean define la configuración de CORS
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permite peticiones desde CUALQUIER origen.
        // Para más seguridad, reemplaza "*" con la URL de tu Replit.
        configuration.setAllowedOrigins(List.of("*")); 
        
        // Permite los métodos HTTP más comunes
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Permite cabeceras comunes
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica esta config a TODAS las rutas
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
