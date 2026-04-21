package es.pcomida.proyectocomida.pedido.services;

import es.pcomida.proyectocomida.pedido.dto.PedidoCreateDto;
import es.pcomida.proyectocomida.pedido.dto.PedidoResponseDto;
import es.pcomida.proyectocomida.pedido.dto.PedidoUpdateDto;
import es.pcomida.proyectocomida.pedido.exceptions.PedidoNotFoundException;
import es.pcomida.proyectocomida.pedido.mapper.PedidoMapper;
import es.pcomida.proyectocomida.pedido.models.Estado;
import es.pcomida.proyectocomida.pedido.models.Pedido;
import es.pcomida.proyectocomida.pedido.repositories.PedidosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = "pedidos")
public class PedidosServiceImpl implements PedidosService {
    private final PedidosRepository pedidosRepository;
    private final PedidoMapper pedidoMapper;

    @Override
    public Page<PedidoResponseDto> findAll(Optional<Long> usuario, Optional<Estado> estado, Pageable pageable) {
        log.info("Buscando pedidos con filtros: usuario={}, estado={}", usuario, estado);

        Specification<Pedido> specUsuario = (root, query, cb) ->
                usuario.map(u -> cb.equal(root.get("usuario"), u))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Pedido> specEstado = (root, query, cb) ->
                estado.map(e -> cb.equal(root.get("estado"), e))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Pedido> specIsDeleted = (root, query, cb) ->
                cb.equal(root.get("isDeleted"), false); // Asumimos que por defecto solo mostramos los no borrados

        Specification<Pedido> criterio = Specification.where(specUsuario)
                .and(specEstado)
                .and(specIsDeleted);

        Page<Pedido> pedidoPage = pedidosRepository.findAll(criterio, pageable);
        return pedidoPage.map(pedidoMapper::toPedidoResponseDto);
    }

    @Override
    @Cacheable(key = "#id")
    public PedidoResponseDto findById(Long id) {
        log.info("Buscando producto por id: " + id);
        return pedidoMapper.toPedidoResponseDto(pedidosRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException(id)));
    }


    @CachePut
    @Override
    public PedidoResponseDto save(PedidoCreateDto pedidoCreateDto) {
        log.info("Guardando producto: " + pedidoCreateDto);

        Pedido nuevoPedido = pedidoMapper.toPedido(pedidoCreateDto);
        return pedidoMapper.toPedidoResponseDto(pedidosRepository.save(nuevoPedido));
    }

    @CachePut
    @Override
    public PedidoResponseDto update(Long id, PedidoUpdateDto pedidoUpdateDto) {
        log.info("Actualizando producto por id: " + id);
        // Si no existe lanza excepción, por eso ya llamamos a lo que hemos implementado antes
        var PedidoActual = pedidosRepository.findById(id).orElseThrow(() -> new PedidoNotFoundException(id));
        // Actualizamos el producto con los datos que nos vienen del dto, podríamos usar el mapper
        Pedido productoActualizado = pedidoMapper.toPedido(pedidoUpdateDto, PedidoActual);
        // Lo guardamos en el repositorio
        return pedidoMapper.toPedidoResponseDto(pedidosRepository.save(productoActualizado));
    }

    @Override
    @CacheEvict
    public void deleteById(Long id) {
        log.debug("Borrando producto por id: " + id);
        // Si no existe lanza excepción, por eso ya llamamos a lo que hemos implementado antes
        this.findById(id);
        // Lo borramos del repositorio
        pedidosRepository.deleteById(id);

    }
}
