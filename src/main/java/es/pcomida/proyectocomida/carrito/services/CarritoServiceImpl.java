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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 1. Determinamos el ID final que usaremos para filtrar
        final Long idParaFiltrar;

        if (user.getRoles().stream().noneMatch(r -> r.name().equals("ADMIN"))) {
            // Si no es ADMIN, forzamos que solo vea lo suyo
            idParaFiltrar = user.getId();
        } else {
            // Si es ADMIN, usamos el que venga por parámetro o null si no viene nada
            idParaFiltrar = usuarioId.orElse(null);
        }

        log.info("Buscando carritos con filtros: usuarioId={}, estado={}, isDeleted={}", idParaFiltrar, estado, isDeleted);

        // 2. Usamos la variable final dentro de las especificaciones
        Specification<Carrito> specUsuario = (root, query, cb) ->
                idParaFiltrar != null ? cb.equal(root.get("usuario").get("id"), idParaFiltrar) : null;

        Specification<Carrito> specEstado = (root, query, cb) ->
                estado.isPresent() ? cb.equal(root.get("estado"), estado.get()) : null;

        Specification<Carrito> specIsDeleted = (root, query, cb) ->
                isDeleted.isPresent() ? cb.equal(root.get("isDeleted"), isDeleted.get()) : null;

        Specification<Carrito> criterio = Specification.where(specUsuario)
                .and(specEstado)
                .and(specIsDeleted);

        Page<Carrito> carritoPage = carritoRepository.findAll(criterio, pageable);
        return carritoPage.map(carritoMapper::toCarritoResponseDTO);
    }

    @Override
    @Cacheable(key = "#id")
    public CarritoResponseDTO findById(Long id) {
        log.info("Buscando Carrito por id: {}", id);
        Carrito carrito = carritoRepository.findById(id).orElseThrow(() -> new CarritoNotFoundException(id));
        checkAdminOrOwner(carrito.getUsuario().getId());
        return carritoMapper.toCarritoResponseDTO(carrito);
    }

    @Override
    @Transactional
    @CachePut(key = "#carritoId")
    public CarritoResponseDTO addPlatoToCarrito(Long carritoId, AddCarritoItemDTO itemDTO) {
        log.info("Añadiendo plato {} al carrito {}", itemDTO.getPlatoID(), carritoId);
        Carrito carritoActual = carritoRepository.findById(carritoId).orElseThrow(() -> new CarritoNotFoundException(carritoId));
        checkAdminOrOwner(carritoActual.getUsuario().getId());

        Plato platoAdd = platosRepository.findById(itemDTO.getPlatoID()).orElseThrow(() -> new PlatoNotFoundException(itemDTO.getPlatoID()));

        var platoDuplicado = carritoItemRepository.findByCarritoIdAndPlatoId(carritoId, itemDTO.getPlatoID());
        if (platoDuplicado.isPresent()) {
            CarritoItem itemExist = platoDuplicado.get();
            itemExist.setCantidad(itemExist.getCantidad() + itemDTO.getCantidad());
            itemExist.setPrecioUnitario(platoAdd.getPrecio());
            carritoItemRepository.save(itemExist);
        } else {
            CarritoItem itemNew = CarritoItem.builder()
                    .carrito(carritoActual)
                    .plato(platoAdd)
                    .cantidad(itemDTO.getCantidad())
                    .precioUnitario(platoAdd.getPrecio())
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
        log.info("Guardando Carrito: {}", carritoCreateDto);
        Usuario usuario = usuarioRepository.findById(carritoCreateDto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        checkAdminOrOwner(usuario.getId());
        
        Carrito nuevoCarrito = carritoMapper.toCarrito(carritoCreateDto, usuario);
        return carritoMapper.toCarritoResponseDTO(carritoRepository.save(nuevoCarrito));
    }

    @Override
    @Transactional
    @CachePut(key = "#id")
    public CarritoResponseDTO update(Long id, CarritoUpdateDto carritoUpdateDto) {
        log.info("Actualizando Carrito por id: {}", id);
        Carrito carritoActual = carritoRepository.findById(id).orElseThrow(() -> new CarritoNotFoundException(id));
        checkAdminOrOwner(carritoActual.getUsuario().getId());
        
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
    @Transactional
    public void deleteById(Long id) {
        log.debug("Borrando (soft delete) Carrito por id: {}", id);
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new CarritoNotFoundException(id));
        checkAdminOrOwner(carrito.getUsuario().getId());

        // Soft delete real — igual que el resto de entidades
        carrito.setIsDeleted(true);
        carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#carritoId")
    public void deleteItemFromCarrito(Long carritoId, Long id) {
        log.info("Borrando plato del carrito con id: {}", id);
        CarritoItem itemDelete = carritoItemRepository.findById(id).orElseThrow(() -> new PlatoNotFoundException(id));
        checkAdminOrOwner(itemDelete.getCarrito().getUsuario().getId());

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

    private void checkAdminOrOwner(Long ownerId) {
        Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRoles().stream().noneMatch(r -> r.name().equals("ADMIN")) && !user.getId().equals(ownerId)) {
            throw new AccessDeniedException("No tienes permiso para realizar esta operación sobre un recurso que no te pertenece.");
        }
    }
}
