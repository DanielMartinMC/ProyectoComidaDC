package es.pcomida.proyectocomida.pedido.services;

import es.pcomida.proyectocomida.carrito.exceptions.CarritoNotFoundException;
import es.pcomida.proyectocomida.carrito.models.Carrito;
import es.pcomida.proyectocomida.carrito.models.Estados;
import es.pcomida.proyectocomida.carrito.repositories.CarritoRepository;
import es.pcomida.proyectocomida.pedido.dto.CheckoutRequestDto;
import es.pcomida.proyectocomida.pedido.dto.PedidoResponseDto;
import es.pcomida.proyectocomida.pedido.dto.PedidoUpdateDto;
import es.pcomida.proyectocomida.pedido.exceptions.PedidoNotFoundException;
import es.pcomida.proyectocomida.pedido.mapper.PedidoMapper;
import es.pcomida.proyectocomida.pedido.models.Estado;
import es.pcomida.proyectocomida.pedido.models.Pedido;
import es.pcomida.proyectocomida.pedido.repositories.PedidosRepository;
import es.pcomida.proyectocomida.usuario.models.Usuario;
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

import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = "pedidos")
public class PedidosServiceImpl implements PedidosService {

    private final PedidosRepository pedidosRepository;
    private final PedidoMapper pedidoMapper;
    private final CarritoRepository carritoRepository;

    @Override
    public Page<PedidoResponseDto> findAll(Optional<Long> usuarioId, Optional<Estado> estado,
                                           Optional<Date> fechaDesde, Optional<Date> fechaHasta, Pageable pageable) {

        Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final Optional<Long> idUser;
        if (user.getRoles().stream().anyMatch(r -> r.name().equals("ADMIN"))) {
            idUser = usuarioId; // Admin puede filtrar por cualquier usuario o ver todos
        } else {
            idUser = Optional.of(user.getId()); // User solo ve los suyos siempre
        }

        log.info("Buscando pedidos con filtros: usuarioId={}, estado={}", idUser, estado);

        Specification<Pedido> specUsuario = (root, query, cb) ->
                idUser.map(u -> cb.equal(root.get("usuario").get("id"), u))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Pedido> specEstado = (root, query, cb) ->
                estado.map(e -> cb.equal(root.get("estado"), e))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Pedido> specFechaDesde = (root, query, cb) ->
                fechaDesde.map(f -> cb.lessThanOrEqualTo(root.get("fechaPedido"), f))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Pedido> specFechaHasta = (root, query, cb) ->
                fechaHasta.map(f -> cb.greaterThanOrEqualTo(root.get("fechaPedido"), f))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Pedido> specIsDeleted = (root, query, cb) ->
                cb.equal(root.get("isDeleted"), false);

        Specification<Pedido> criterio = Specification.where(specUsuario)
                .and(specEstado)
                .and(specFechaDesde)
                .and(specFechaHasta)
                .and(specIsDeleted);

        return pedidosRepository.findAll(criterio, pageable)
                .map(pedidoMapper::toPedidoResponseDto);
    }

    @Override
    @Cacheable(key = "#id")
    public PedidoResponseDto findById(Long id) {
        log.info("Buscando pedido por id: {}", id);
        Pedido pedido = pedidosRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException(id));
        checkAdminOrOwner(pedido.getUsuario().getId());
        return pedidoMapper.toPedidoResponseDto(pedido);
    }

    @Override
    @Transactional
    @CachePut(key = "#result.id")
    public PedidoResponseDto save(CheckoutRequestDto checkoutRequestDto) {
        log.info("Procesando checkout para el carrito id: {}", checkoutRequestDto.getCarritoId());

        Carrito carrito = carritoRepository.findById(checkoutRequestDto.getCarritoId())
                .orElseThrow(() -> new CarritoNotFoundException(checkoutRequestDto.getCarritoId()));

        checkAdminOrOwner(carrito.getUsuario().getId());

        if (carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío, no se puede crear un pedido.");
        }

        Pedido nuevoPedido = pedidoMapper.toPedido(carrito);
        nuevoPedido.setEstado(Estado.EnProceso);

        carrito.setEstado(Estados.Contenido);
        carritoRepository.save(carrito);

        return pedidoMapper.toPedidoResponseDto(pedidosRepository.save(nuevoPedido));
    }

    @Override
    @CachePut(key = "#id")
    public PedidoResponseDto update(Long id, PedidoUpdateDto pedidoUpdateDto) {
        log.info("Actualizando pedido por id: {}", id);
        Pedido pedidoActual = pedidosRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException(id));
        checkAdminOrOwner(pedidoActual.getUsuario().getId());

        Pedido pedidoActualizado = pedidoMapper.toPedido(pedidoUpdateDto, pedidoActual);
        return pedidoMapper.toPedidoResponseDto(pedidosRepository.save(pedidoActualizado));
    }

    @Override
    @CacheEvict(key = "#id")
    @Transactional
    public void deleteById(Long id) {
        log.debug("Borrando (soft delete) pedido por id: {}", id);
        Pedido pedido = pedidosRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException(id));
        checkAdminOrOwner(pedido.getUsuario().getId());

        // ✅ Soft delete real en vez de borrado físico
        pedido.setIsDeleted(true);
        pedidosRepository.save(pedido);
    }

    // --- Métodos privados ---

    private void checkAdminOrOwner(Long ownerId) {
        Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRoles().stream().noneMatch(r -> r.name().equals("ADMIN"))
                && !user.getId().equals(ownerId)) {
            throw new AccessDeniedException(
                    "No tienes permiso para realizar esta operación sobre un recurso que no te pertenece.");
        }
    }
}
