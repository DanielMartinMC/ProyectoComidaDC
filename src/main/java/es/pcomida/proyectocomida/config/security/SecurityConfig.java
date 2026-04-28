package es.pcomida.proyectocomida.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Habilita el uso de @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Rutas Públicas (Sin token)
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers("/v1/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/platos/**").permitAll()

                        // 2. Rutas EXCLUSIVAS para el ADMIN
                        // Solo admin puede ver la lista general de todos los usuarios
                        .requestMatchers(HttpMethod.GET, "/v1/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/usuarios/**").authenticated()
                        // Solo admin puede ver la lista general de todos los carritos
                        .requestMatchers(HttpMethod.GET, "/v1/carritos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/carritos/**").authenticated()
                        // Solo admin puede crear, modificar o borrar platos
                        .requestMatchers(HttpMethod.POST, "/v1/platos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/v1/platos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/v1/platos/**").hasRole("ADMIN")

                        // 3. El resto de peticiones requieren autenticación normal
                        // (Ej: Un usuario normal viendo su propio perfil o modificando su propio carrito)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        // Necesario para la consola H2
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}
