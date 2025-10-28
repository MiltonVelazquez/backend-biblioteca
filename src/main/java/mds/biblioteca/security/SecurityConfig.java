// src/main/java/com/utn/frre/biblioteca/security/SecurityConfig.java
package mds.biblioteca.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    // (Aquí irían JwtAuthenticationFilter y UserDetailsService)

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para APIs REST
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Política sin estado (JWT)
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (ej. login, registro)
                .requestMatchers("/api/v1/auth/**").permitAll() 
                .requestMatchers("/api/v1/socios/verificar-dni/**").permitAll()
                .requestMatchers("/api/v1/socios/alta").permitAll()
                
                // El resto de endpoints requieren autenticación
                .anyRequest().permitAll()
            );
            // (Aquí se agregaría el filtro de JWT)
            // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}