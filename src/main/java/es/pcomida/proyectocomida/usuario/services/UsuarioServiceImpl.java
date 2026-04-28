package es.pcomida.proyectocomida.usuario.services;

import es.pcomida.proyectocomida.carrito.models.Carrito;
import es.pcomida.proyectocomida.carrito.models.Estados;
import es.pcomida.proyectocomida.carrito.repositories.CarritoRepository;
import es.pcomida.proyectocomida.usuario.dto.UsuarioCreateDTO;
import es.pcomida.proyectocomida.usuario.dto.UsuarioResponseDTO;
import es.pcomida.proyectocomida.usuario.dto.UsuarioUpdateDTO;
import es.pcomida.proyectocomida.usuario.exceptions.UsuarioBadRequestException;
import es.pcomida.proyectocomida.usuario.exceptions.UsuarioNotFoundException;
import es.pcomida.proyectocomida.usuario.mapper.UsuarioMapper;
import es.pcomida.proyectocomida.usuario.models.Roles;
import es.pcomida.proyectocomida.usuario.models.Usuario;
import es.pcomida.proyectocomida.usuario.repositories.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CacheConfig(cacheNames = {"usuarios"})
@Slf4j
@RequiredArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService, InitializingBean {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final CarritoRepository carritoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UsuarioResponseDTO> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable) {
        log.info("Buscando usuarios con filtros: username={}, email={}, isDeleted={}", username, email, isDeleted);

        Specification<Usuario> specUserName = (root, query, cb) ->
                username.map(u -> cb.like(cb.lower(root.get("username")), "%" + u.toLowerCase() + "%"))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Usuario> specEmail = (root, query, cb) ->
                email.map(e -> cb.like(cb.lower(root.get("email")), "%" + e.toLowerCase() + "%"))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Usuario> specIsDeleted = (root, query, cb) ->
                isDeleted.map(d -> cb.equal(root.get("isDeleted"), d))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Usuario> criterio = Specification.where(specUserName)
                .and(specEmail)
                .and(specIsDeleted);

        Page<Usuario> usuarioPage = usuarioRepository.findAll(criterio, pageable);
        return usuarioPage.map(usuarioMapper::toUsuarioResponseDTO);
    }

    @Override
    public UsuarioResponseDTO findById(Long id) {
        log.info("Buscando Usuarios por id: " + id);
        return usuarioMapper.toUsuarioResponseDTO(usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id)));
    }

    @Override
    @Transactional
    @CachePut(key = "#result.id")
    public UsuarioResponseDTO save(UsuarioCreateDTO usuarioCreateDTO) {
        throw new UnsupportedOperationException("La creación de usuarios debe hacerse a través del endpoint de registro /signup.");
    }

    @Override
    @Transactional
    public UsuarioResponseDTO update(Long id, UsuarioUpdateDTO usuarioUpdateDTO) {
        log.info("Actualizando usuarios con el id: " + id);
        var usuarioActual = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id));

        if (usuarioUpdateDTO.getUsername() != null && !usuarioActual.getUsername().equals(usuarioUpdateDTO.getUsername()) && usuarioRepository.existsByUsername(usuarioUpdateDTO.getUsername())) {
            throw new UsuarioBadRequestException("El username ya existe");
        }
        if (usuarioUpdateDTO.getEmail() != null && !usuarioActual.getEmail().equals(usuarioUpdateDTO.getEmail()) && usuarioRepository.existsByEmail(usuarioUpdateDTO.getEmail())) {
            throw new UsuarioBadRequestException("El email ya existe");
        }
        
        if (usuarioUpdateDTO.getPassword() != null && !usuarioUpdateDTO.getPassword().isEmpty()) {
            usuarioUpdateDTO.setPassword(passwordEncoder.encode(usuarioUpdateDTO.getPassword()));
        }

        Usuario usuarioActualizado = usuarioMapper.toUsuario(usuarioUpdateDTO, usuarioActual);
        Usuario usuarioGuardado = usuarioRepository.save(usuarioActualizado);
        
        return usuarioMapper.toUsuarioResponseDTO(usuarioGuardado);
    }

    @Override
    @Transactional
    @CachePut(key = "#id")
    public void deleteById(Long id) {
        log.info("Borrando usuario por el id: " + id);
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException(id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public void softDeleteById(Long id) {
        log.info("Borrando lógicamente el usuario con id: " + id);
        Usuario usuario = usuarioRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
        usuario.setIsDeleted(true);
        usuario.setUpdatedAt(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    @CachePut(key = "#id")
    public UsuarioResponseDTO subscribe(Long id) {
        log.info("Suscribiendo al usuario con id: {}", id);
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id));
        usuario.setIsSuscriptor(true);
        return usuarioMapper.toUsuarioResponseDTO(usuarioRepository.save(usuario));
    }

    @PostConstruct
    @Transactional
    public void afterPropertiesSet() {
        log.info("Inicializando datos de la aplicación...");
    }
}
