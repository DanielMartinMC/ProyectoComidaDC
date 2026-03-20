package es.pcomida.proyectocomida.Usuario.services;

import es.pcomida.proyectocomida.Carrito.models.Carrito;
import es.pcomida.proyectocomida.Carrito.models.Estados;
import es.pcomida.proyectocomida.Carrito.repositories.CarritoRepository;
import es.pcomida.proyectocomida.Usuario.dto.UsuarioCreateDTO;
import es.pcomida.proyectocomida.Usuario.dto.UsuarioResponseDTO;
import es.pcomida.proyectocomida.Usuario.dto.UsuarioUpdateDTO;
import es.pcomida.proyectocomida.Usuario.exceptios.UsuarioBadRequestException;
import es.pcomida.proyectocomida.Usuario.exceptios.UsuarioNotFoundException;
import es.pcomida.proyectocomida.Usuario.mapper.UsuarioMapper;
import es.pcomida.proyectocomida.Usuario.models.Roles;
import es.pcomida.proyectocomida.Usuario.models.Usuario;
import es.pcomida.proyectocomida.Usuario.repositories.UsuarioRepository;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@CacheConfig(cacheNames = {"usuarios"})
@Slf4j
@RequiredArgsConstructor
@Service
public class UsuariosServicesImpl implements UsuarioService, InitializingBean {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final CarritoRepository carritoRepository;

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
        log.info("Guardando Usuario: " + usuarioCreateDTO);
        if (usuarioRepository.existsByUsername(usuarioCreateDTO.getUsername())) {
            throw new UsuarioBadRequestException("El usuario ya existe");
        }
        if (usuarioRepository.existsByEmail(usuarioCreateDTO.getEmail())) {
            throw new UsuarioBadRequestException("El email ya existe");
        }

        Usuario newUsuario = usuarioMapper.toUsuario(usuarioCreateDTO, null);
        Usuario usuarioSave = usuarioRepository.save(newUsuario);

        Carrito carritoVacio = Carrito.builder()
                .usuario(usuarioSave)
                .estado(Estados.Vacio)
                .codigoCupon(null)
                .descuento(0.0)
                .impuestosCalc(0.0)
                .total(0.0)
                .build();
        carritoRepository.save(carritoVacio);

        return usuarioMapper.toUsuarioResponseDTO(usuarioSave);
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

    @PostConstruct
    @Transactional
    public void afterPropertiesSet() {
        if (!usuarioRepository.existsByUsername("admin")) {
            log.info("Creando usuario ADMIN por defecto...");
            Usuario admin = Usuario.builder()
                    .nombre("Administrador")
                    .apellidos("Sistema")
                    .username("admin")
                    .email("admin@proyectocomida.es")
                    // CORREGIDO: Contraseña más larga y teléfono añadido
                    .password("admin123456") // Ahora cumple la longitud mínima de 10
                    .telefono("123456789")   // Teléfono añadido
                    .direccion("Calle Principal 1")
                    .codigoPostal("28001")
                    .ciudad("Madrid")
                    .pais("España")
                    .roles(Set.of(Roles.ADMIN, Roles.USER))
                    .isDeleted(false)
                    .build();
            Usuario adminGuardado = usuarioRepository.save(admin);

            Carrito carritoVacio = Carrito.builder()
                    .usuario(adminGuardado)
                    .estado(Estados.Vacio)
                    .codigoCupon(null)
                    .descuento(0.0)
                    .impuestosCalc(0.0)
                    .total(0.0)
                    .build();
            carritoRepository.save(carritoVacio);
        }
    }
}
