package es.pcomida.proyectocomida.usuario.services;

import es.pcomida.proyectocomida.usuario.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Cargando usuario por username desde CustomUserDetailsService: {}", username);
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con username: {}", username);
                    return new UsernameNotFoundException("Usuario no encontrado con username: " + username);
                });
    }
}
