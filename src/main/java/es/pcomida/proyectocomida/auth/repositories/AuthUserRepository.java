package es.pcomida.proyectocomida.auth.repositories;

import es.pcomida.proyectocomida.usuario.models.Usuario;

import java.util.Optional;

public interface AuthUserRepository {
    Optional<Usuario> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Usuario save(Usuario usuario);
}
