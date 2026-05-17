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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // allowedOriginPatterns permite wildcards, cubriendo producción y todas las previews de Vercel
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:4200",
                "https://proyecto-comida-zeta.vercel.app"
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        configuration.setExposedHeaders(List.of("link"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/error/**").permitAll();
                    auth.requestMatchers("/v1/auth/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/v1/platos/**").permitAll();

                    if (activeProfile.contains("dev")) {
                        auth.requestMatchers("/h2-console/**").permitAll();
                    }

                    auth.requestMatchers(HttpMethod.GET, "/v1/usuarios").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/v1/usuarios/**").authenticated();
                    auth.requestMatchers(HttpMethod.GET, "/v1/carritos").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/v1/carritos/**").authenticated();
                    auth.requestMatchers(HttpMethod.POST, "/v1/platos/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/v1/platos/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/v1/platos/**").hasRole("ADMIN");

                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        if (activeProfile.contains("dev")) {
            http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        }

        return http.build();
    }
}
