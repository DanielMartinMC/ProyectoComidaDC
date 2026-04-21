package es.pcomida.proyectocomida.carrito.services;

import es.pcomida.proyectocomida.carrito.dto.CarritoCreateDto;
import es.pcomida.proyectocomida.carrito.dto.CarritoResponseDTO;
import es.pcomida.proyectocomida.carrito.dto.CarritoUpdateDto;
import es.pcomida.proyectocomida.carrito.exceptions.CarritoNotFoundException;
import es.pcomida.proyectocomida.carrito.mapper.CarritoMapper;
import es.pcomida.proyectocomida.carrito.models.Carrito;
import es.pcomida.proyectocomida.carrito.models.Estados;
import es.pcomida.proyectocomida.carrito.repositories.CarritoRepository;
import es.pcomida.proyectocomida.carritoitem.dto.AddCarritoItemDTO;
import es.pcomida.proyectocomida.carritoitem.models.CarritoItem;
import es.pcomida.proyectocomida.carritoitem.repositories.CarritoItemRepository;
import es.pcomida.proyectocomida.plato.exceptions.PlatoNotFoundException;
import es.pcomida.proyectocomida.plato.models.Plato;
import es.pcomida.proyectocomida.plato.repositories.PlatosRepository;
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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = "carrito")
public class CarritoServiceImpl implements CarritoService {
    private final CarritoMapper carritoMapper;
    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final PlatosRepository platosRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public Page<CarritoResponseDTO> findAll(Optional<Long> usuarioId, Optional<Estados> estado, Optional<Boolean> isDeleted, Pageable pageable) {
        log.info("Buscando carritos con filtros: usuarioId={}, estado={}, isDeleted={}", usuarioId, estado, isDeleted);

        Specification<Carrito> specUsuario = (root, query, cb) ->
                usuarioId.map(u -> cb.equal(root.get("usuario").get("id"), u))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Carrito> specEstado = (root, query, cb) ->
                estado.map(e -> cb.equal(root.get("estado"), e))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Carrito> specIsDeleted = (root, query, cb) ->
                isDeleted.map(d -> cb.equal(root.get("isDeleted"), d))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Carrito> criterio = Specification.where(specUsuario)
                .and(specEstado)
                .and(specIsDeleted);

        Page<Carrito> carritoPage = carritoRepository.findAll(criterio, pageable);
        return carritoPage.map(carritoMapper::toCarritoResponseDTO);
    }

    @Override
    @Cacheable(key = "#id")
    public CarritoResponseDTO findById(Long id) {
        log.info("Buscando Carrito por id: " + id);
        return carritoMapper.toCarritoResponseDTO(carritoRepository.findById(id).orElseThrow(() -> new CarritoNotFoundException(id)));
    }

    @Override
    @Transactional
    @CachePut(key = "#carritoId")
    public CarritoResponseDTO addPlatoToCarrito(Long carritoId, AddCarritoItemDTO itemDTO) {
        log.info("Añadiendo plato {} al carrito {}", itemDTO.getPlatoID(), carritoId);

        Carrito carritoActual = carritoRepository.findById(carritoId).orElseThrow(() -> new CarritoNotFoundException(carritoId));
        Plato platoAdd = platosRepository.findById(itemDTO.getPlatoID()).orElseThrow(() -> new PlatoNotFoundException(itemDTO.getPlatoID()));

        var platoDuplicado = carritoItemRepository.findByCarritoIdAndPlatoId(carritoId, itemDTO.getPlatoID());
        if (platoDuplicado.isPresent()) {
            CarritoItem itemExist = platoDuplicado.get();
            itemExist.setCantidad(itemExist.getCantidad() + itemDTO.getCantidad());
            // Actualizamos el precio unitario al actual por si ha cambiado (opcional, depende de la política de negocio)
            itemExist.setPrecioUnitario(platoAdd.getPrecio());
            carritoItemRepository.save(itemExist);
        } else {
            CarritoItem itemNew = CarritoItem.builder()
                    .carrito(carritoActual)
                    .plato(platoAdd)
                    .cantidad(itemDTO.getCantidad())
                    .precioUnitario(platoAdd.getPrecio()) // Guardamos el precio actual
                    .build();
            carritoItemRepository.save(itemNew);
            carritoActual.getItems().add(itemNew);
        }
        
        carritoActual.setEstado(Estados.Contenido);
        carritoActual.recalcularTotales();

        return carritoMapper.toCarritoResponseDTO(carritoRepository.save(carritoActual));
    }

    @Override
    @Transactional
    @CachePut(key = "#result.id")
    public CarritoResponseDTO save(CarritoCreateDto carritoCreateDto) {
        log.info("Guardando Carrito: " + carritoCreateDto);
        Usuario usuario = usuarioRepository.findById(carritoCreateDto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Carrito nuevoCarrito = carritoMapper.toCarrito(carritoCreateDto, usuario);
        return carritoMapper.toCarritoResponseDTO(carritoRepository.save(nuevoCarrito));
    }

    @Override
    @Transactional
    @CachePut(key = "#id")
    public CarritoResponseDTO update(Long id, CarritoUpdateDto carritoUpdateDto) {
        log.info("Actualizando Carrito por id: " + id);
        Carrito carritoActual = carritoRepository.findById(id).orElseThrow(() -> new CarritoNotFoundException(id));
        
        if (carritoUpdateDto.getCodigoCupon() != null) {
            carritoActual.setCodigoCupon(carritoUpdateDto.getCodigoCupon());
            if ("VERANO".equalsIgnoreCase(carritoUpdateDto.getCodigoCupon())) {
                carritoActual.setDescuento(10.0);
            } else {
                carritoActual.setDescuento(0.0);
            }
        }

        carritoActual.recalcularTotales();
        return carritoMapper.toCarritoResponseDTO(carritoRepository.save(carritoActual));
    }

    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        log.debug("Borrando Carrito por id: " + id);
        if (!carritoRepository.existsById(id)) {
            throw new CarritoNotFoundException(id);
        }
        carritoRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#carritoId")
    public void deleteItemFromCarrito(Long carritoId, Long id) {
        log.info("Borrando plato del carrito con id: {}", id);
        CarritoItem itemDelete = carritoItemRepository.findById(id).orElseThrow(() -> new PlatoNotFoundException(id));

        if (!itemDelete.getCarrito().getId().equals(carritoId)) {
            throw new CarritoNotFoundException(carritoId);
        }
        
        Carrito carrito = itemDelete.getCarrito();
        carrito.getItems().remove(itemDelete);
        carritoItemRepository.delete(itemDelete);
        
        carrito.recalcularTotales();
        if (carrito.getItems().isEmpty()) {
            carrito.setEstado(Estados.Vacio);
        }
        carritoRepository.save(carrito);
    }
}
