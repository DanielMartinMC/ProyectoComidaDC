package es.pcomida.proyectocomida.Pedido.services;

import es.pcomida.proyectocomida.Pedido.dto.PedidoCreateDto;
import es.pcomida.proyectocomida.Pedido.dto.PedidoResponseDto;
import es.pcomida.proyectocomida.Pedido.dto.PedidoUpdateDto;
import es.pcomida.proyectocomida.Pedido.exceptions.PedidoNotFoundException;
import es.pcomida.proyectocomida.Pedido.mapper.PedidoMapper;
import es.pcomida.proyectocomida.Pedido.models.Estado;
import es.pcomida.proyectocomida.Pedido.models.Pedido;
import es.pcomida.proyectocomida.Pedido.repositories.PedidosRepository;
import es.pcomida.proyectocomida.Plato.dto.PlatoCreateDto;
import es.pcomida.proyectocomida.Plato.dto.PlatoResponseDto;
import es.pcomida.proyectocomida.Plato.dto.PlatoUpdateDto;
import es.pcomida.proyectocomida.Plato.exceptions.PlatoNotFoundException;
import es.pcomida.proyectocomida.Plato.mapper.PlatoMapper;
import es.pcomida.proyectocomida.Plato.models.Plato;
import es.pcomida.proyectocomida.Plato.models.Tipo;
import es.pcomida.proyectocomida.Plato.repositories.PlatosRepository;
import es.pcomida.proyectocomida.Plato.services.PlatosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = "pedidos")
public class PedidosServiceImpl implements PedidosService {
    private final PedidosRepository pedidosRepository;
    private final PedidoMapper pedidoMapper;

    @Override
    public List<PedidoResponseDto> findAll(Long usuario, Estado estado) {
        // Si todo está vacío o nulo, devolvemos todos los Platos
        if ((usuario == null) && (estado == null)) {
            log.info("Buscando todos los Pedidos");
            return pedidoMapper.toResponseDtoList(pedidosRepository.findAll());
        }
        // Si la nombre no está vacía, pero la categoría si, buscamos por nombre
        if ((usuario != null) && (estado == null)) {
            log.info("Buscando productos por usuario: " + usuario);
            return pedidoMapper.toResponseDtoList(pedidosRepository.findByUsuario(usuario));
        }
        // Si la nombre está vacía, pero la categoría no, buscamos por categoría
        if (usuario == null) {
            log.info("Buscando productos por estado: " + estado);
            return pedidoMapper.toResponseDtoList(pedidosRepository.findByEstadoContainsIgnoreCase(estado));
        }
        // Si la nombre y la categoría no están vacías, buscamos por ambas
        log.info("Buscando productos por usuario: " + usuario + " y estado: " + estado);
        return pedidoMapper.toResponseDtoList(pedidosRepository.findByUsuarioAndEstadoContainsIgnoreCase(usuario, estado));
    }

    @Override
    @Cacheable(key = "#id")
    public PedidoResponseDto findById(Long id) {
        log.info("Buscando producto por id: " + id);
        return pedidoMapper.toPedidoResponseDto(pedidosRepository.findById(id)
                .orElseThrow(() -> new PlatoNotFoundException(id)));
    }


    @CachePut
    @Override
    public PedidoResponseDto save(PedidoCreateDto pedidoCreateDto) {
        log.info("Guardando producto: " + pedidoCreateDto);
        // obtenemos el id de producto
        // Creamos el producto nuevo con los datos que nos vienen del dto, podríamos usar el mapper
        Pedido nuevoPedido = pedidoMapper.toPedido(pedidoCreateDto);
        // Lo guardamos en el repositorio
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
