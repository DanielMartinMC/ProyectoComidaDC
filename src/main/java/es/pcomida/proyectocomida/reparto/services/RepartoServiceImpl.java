package es.pcomida.proyectocomida.reparto.services;

import es.pcomida.proyectocomida.pedido.exceptions.PedidoNotFoundException;
import es.pcomida.proyectocomida.pedido.models.Pedido;
import es.pcomida.proyectocomida.pedido.repositories.PedidosRepository;
import es.pcomida.proyectocomida.reparto.dto.RepartoCreateDto;
import es.pcomida.proyectocomida.reparto.dto.RepartoResponseDto;
import es.pcomida.proyectocomida.reparto.dto.RepartoUpdateDto;
import es.pcomida.proyectocomida.reparto.exceptions.RepartoNotFoundException;
import es.pcomida.proyectocomida.reparto.mapper.RepartoMapper;
import es.pcomida.proyectocomida.reparto.models.Reparto;
import es.pcomida.proyectocomida.reparto.models.RepartoEstado;
import es.pcomida.proyectocomida.reparto.repositories.RepartoRepository;
import es.pcomida.proyectocomida.usuario.exceptions.UsuarioNotFoundException;
import es.pcomida.proyectocomida.usuario.models.Roles;
import es.pcomida.proyectocomida.usuario.models.Usuario;
import es.pcomida.proyectocomida.usuario.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@CacheConfig(cacheNames = {"repartos"})
@Slf4j
@RequiredArgsConstructor
@Service
public class RepartoServiceImpl implements RepartoService {
    private final RepartoRepository repartoRepository;
    private final RepartoMapper repartoMapper;
    private final PedidosRepository pedidosRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public Page<RepartoResponseDto> findAll(Optional<RepartoEstado> estado, Optional<String> ciudad, Optional<Long> repartidorId, Pageable pageable) {
        log.info("Buscando repartos con los filtros: estado={}, ciudad={}, repartidorId={}", estado, ciudad, repartidorId);
        Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRoles().stream().noneMatch(r -> r.name().equals("ADMIN"))) {
            throw new AccessDeniedException("No tienes permiso para buscar todos los repartos.");
        }

        Specification<Reparto> specRepartoStatus = (root, query, cb) ->
                estado.map(e -> cb.equal(root.get("estado"), e))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));
        Specification<Reparto> specRepartoCiudad = (root, query, cb) ->
                ciudad.map(c -> cb.like(root.get("ciudad"), c))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));
        Specification<Reparto> specRepartoRepartidor = (root, query, cb) ->
                repartidorId.map(r -> cb.equal(root.get("repartidorID"), r))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Reparto> criterio = Specification.where(specRepartoStatus)
                .and(specRepartoCiudad).and(specRepartoRepartidor);

        Page<Reparto> repartoPage = repartoRepository.findAll(criterio, pageable);
        return repartoPage.map(repartoMapper::toRepartoResponseDto);
    }

    @Override
    @Cacheable(key = "#id")
    public RepartoResponseDto findById(Long id) {
        log.info("Buscando reparto con id: {}", id);
        Reparto reparto = repartoRepository.findById(id).orElseThrow(() -> new RepartoNotFoundException(id));
        checkAdminOrOwnerOrRepartidor(reparto);
        return repartoMapper.toRepartoResponseDto(reparto);
    }

    @Override
    @Transactional
    @CachePut(key = "#result.id")
    public RepartoResponseDto save(RepartoCreateDto repartoCreateDto) {
        log.info("Guardando nuevo reparto");
        checkIsAdmin();
        Usuario usuario = usuarioRepository.findById(repartoCreateDto.getUsuario().getId())
                .orElseThrow(() -> new UsuarioNotFoundException(repartoCreateDto.getUsuario().getId()));
        Pedido pedido = pedidosRepository.findById(repartoCreateDto.getPedido_id().getId())
                .orElseThrow(() -> new PedidoNotFoundException(repartoCreateDto.getPedido_id().getId()));
        
        Reparto reparto = repartoMapper.toReparto(repartoCreateDto, usuario, pedido);
        Reparto savedReparto = repartoRepository.save(reparto);
        return repartoMapper.toRepartoResponseDto(savedReparto);
    }

    @Override
    @Transactional
    @CachePut(key = "#id")
    public RepartoResponseDto update(Long id, RepartoUpdateDto repartoUpdateDto) {
        log.info("Actualizando reparto con id: {}", id);
        Reparto reparto = repartoRepository.findById(id).orElseThrow(() -> new RepartoNotFoundException(id));
        checkAdminOrOwnerOrRepartidor(reparto);

        Reparto updatedReparto = repartoMapper.toReparto(repartoUpdateDto, reparto);
        return repartoMapper.toRepartoResponseDto(repartoRepository.save(updatedReparto));
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        log.info("Borrando reparto con id: {}", id);
        Reparto reparto = repartoRepository.findById(id).orElseThrow(() -> new RepartoNotFoundException(id));
        checkIsAdmin();
        repartoRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CachePut(key = "#repartoId")
    public RepartoResponseDto asignarRepartidor(Long repartoId, Long repartidorId) {
        log.info("Asignando repartidor {} al reparto {}", repartidorId, repartoId);
        checkIsAdmin();
        Reparto reparto = repartoRepository.findById(repartoId).orElseThrow(() -> new RepartoNotFoundException(repartoId));
        
        reparto.setRepartidorID(repartidorId);
        if (reparto.getEstado() == RepartoEstado.PENDIENTE) {
            reparto.setEstado(RepartoEstado.EN_PREPARACION);
        }
        
        Reparto updatedReparto = repartoRepository.save(reparto);
        return repartoMapper.toRepartoResponseDto(updatedReparto);
    }

    @Override
    @Transactional
    @CachePut(key = "#repartoId")
    public RepartoResponseDto actualizarEstado(Long repartoId, RepartoEstado nuevoEstado) {
        log.info("Actualizando estado del reparto {} a {}", repartoId, nuevoEstado);
        Reparto reparto = repartoRepository.findById(repartoId).orElseThrow(() -> new RepartoNotFoundException(repartoId));
        checkAdminOrOwnerOrRepartidor(reparto);
        
        reparto.setEstado(nuevoEstado);
        
        Reparto updatedReparto = repartoRepository.save(reparto);
        return repartoMapper.toRepartoResponseDto(updatedReparto);
    }

    @Override
    @Transactional
    public RepartoResponseDto cancelarReparto(Long repartoId) {
        log.info("Cancelando reparto {}", repartoId);
        Reparto reparto = repartoRepository.findById(repartoId).orElseThrow(() -> new RepartoNotFoundException(repartoId));
        checkAdminOrOwnerOrRepartidor(reparto);

        reparto.setEstado(RepartoEstado.CANCELADO);

        Reparto updatedReparto = repartoRepository.save(reparto);
        return repartoMapper.toRepartoResponseDto(updatedReparto);
    }

    @Override
    public Page<RepartoResponseDto> findRepartosByUsuario(Long usuarioId, Pageable pageable) {
        log.info("Buscando repartos del usuario {}", usuarioId);
        checkAdminOrOwner(usuarioId);
        Page<Reparto> repartos = repartoRepository.findRepartoByUsuarioId(usuarioId, pageable);
        return repartos.map(repartoMapper::toRepartoResponseDto);
    }

    @Override
    public Page<RepartoResponseDto> findRepartosByRepartidor(Long repartidorId, Pageable pageable) {
        log.info("Buscando repartos del repartidor {}", repartidorId);
        checkAdminOrOwner(repartidorId);
        Page<Reparto> repartos = repartoRepository.findRepartoByRepartidorID(repartidorId, pageable);
        return repartos.map(repartoMapper::toRepartoResponseDto);
    }

    private void checkIsAdmin() {
        Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRoles().stream().noneMatch(r -> r.name().equals("ADMIN"))) {
            throw new AccessDeniedException("Esta operación solo puede ser realizada por un administrador.");
        }
    }

    private void checkAdminOrOwner(Long ownerId) {
        Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRoles().stream().noneMatch(r -> r.name().equals("ADMIN")) && !user.getId().equals(ownerId)) {
            throw new AccessDeniedException("No tienes permiso para realizar esta operación sobre un recurso que no te pertenece.");
        }
    }

    private void checkAdminOrOwnerOrRepartidor(Reparto reparto) {
        Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.name().equals("ADMIN"));
        boolean isOwner = user.getId().equals(reparto.getUsuario().getId());
        boolean isRepartidor = reparto.getRepartidorID() != null && user.getId().equals(reparto.getRepartidorID());

        if (!isAdmin && !isOwner && !isRepartidor) {
            throw new AccessDeniedException("No tienes permiso para acceder a este recurso.");
        }
    }
}
