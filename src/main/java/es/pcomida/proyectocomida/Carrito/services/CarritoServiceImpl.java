package es.pcomida.proyectocomida.Carrito.services;


import es.pcomida.proyectocomida.Carrito.dto.CarritoCreateDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoResponseDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoUpdateDto;
import es.pcomida.proyectocomida.Carrito.excepctions.CarritoNotFoundException;
import es.pcomida.proyectocomida.Carrito.mapper.CarritoMapper;
import es.pcomida.proyectocomida.Carrito.models.Carrito;
import es.pcomida.proyectocomida.Carrito.models.Estados;
import es.pcomida.proyectocomida.Carrito.repositories.CarritoRepository;
import es.pcomida.proyectocomida.Carrito_item.dto.AddCarritoItemDTO;
import es.pcomida.proyectocomida.Carrito_item.models.Carrito_item;
import es.pcomida.proyectocomida.Carrito_item.repositories.Carrito_itemRepository;
import es.pcomida.proyectocomida.Plato.exceptions.PlatoNotFoundException;
import es.pcomida.proyectocomida.Plato.models.Plato;
import es.pcomida.proyectocomida.Plato.models.Tipo;
import es.pcomida.proyectocomida.Plato.repositories.PlatosRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = "carrito")
public class CarritoServiceImpl implements  CarritoService {
    private final CarritoMapper carritoMapper;
    private final CarritoRepository carritoRepository;
    private final Carrito_itemRepository carritoItemRepository;
    private final PlatosRepository platosRepository;

    @Override
    public List<CarritoResponseDto> findAll(UUID usuario, Estados estados) {
        if (usuario == null || usuario.toString().isEmpty() && estados == null) {
            log.info("Buscando todos los Carritos");
            return carritoMapper.toResponseDtoList(carritoRepository.findAll());
        }
        if (usuario != null && !usuario.toString().isEmpty() && estados == null) {
            log.info("Buscando Carritos por usuario: " + usuario);
            return carritoMapper.toResponseDtoList(carritoRepository.findByUsuario(usuario));
        }
        if (usuario == null || usuario.toString().isEmpty() && estados != null){
            log.info("Buscando Carritos por tipo: " + estados);
            return carritoMapper.toResponseDtoList(carritoRepository.findByEstadoContainingIgnoreCase(estados));
        }

        log.info("Buscando Carritos por usuario: " + usuario + " y tipo: " + estados);
        return carritoMapper.toResponseDtoList(carritoRepository.findByUsuarioAndEstadoContainsIgnoreCase(usuario, estados));


    }

    @Override
    public CarritoResponseDto findById(Long id) {
        log.info("Buscando Carritos por id: " + id);
        return carritoMapper.toCarritoResponseDto(carritoRepository.findById(id).orElseThrow(() -> new CarritoNotFoundException(id)));
    }

    @Transactional
    public CarritoResponseDto addPlatoToCarrito(Long carritoId, AddCarritoItemDTO itemDTO) {
        log.info("Añadiendo plato {} al carrito {}" + itemDTO.getPlatoID(),carritoId);

        Carrito carritoActual = carritoRepository.findById(carritoId).orElseThrow(() -> new CarritoNotFoundException(carritoId));

        Plato platoAdd = platosRepository.findById(itemDTO.getPlatoID()).orElseThrow(() -> new PlatoNotFoundException(itemDTO.getPlatoID()));

        var platoDuplicado = carritoItemRepository.findByCarritoIdAndPlatoId(itemDTO.getPlatoID(), carritoId);
        if (platoDuplicado.isPresent()) {
            Carrito_item itemExist = platoDuplicado.get();
            itemExist.setCantidad(itemDTO.getCantidad() +itemDTO.getCantidad() );
            carritoItemRepository.save(itemExist);
        }else {
            Carrito_item itemNew = Carrito_item.builder()
                    .carrito(carritoActual)
                    .plato(platoAdd)
                    .cantidad(itemDTO.getCantidad())
                    .build();
            carritoItemRepository.save(itemNew);
            carritoActual.getItems().add(itemNew);
        }
        carritoActual.setEstado(Estados.Contenido);

        return carritoMapper.toCarritoResponseDto(carritoRepository.save(carritoActual));
    }

    @Override
    public CarritoResponseDto save(CarritoCreateDto carritoCreateDto) {
        log.info("Guardando Carrito: " + carritoCreateDto);
        Carrito nuevoCarrito = carritoMapper.toCarrito(carritoCreateDto);
        return carritoMapper.toCarritoResponseDto(carritoRepository.save(nuevoCarrito));
    }

    @Override
    public CarritoResponseDto update(Long id, CarritoUpdateDto carritoUpdateDto) {
        log.info("Buscando Carritos por id: " + id);
        var carritoActual = carritoRepository.findById(id).orElseThrow(() -> new CarritoNotFoundException(id));
        Carrito carritoActualizado = carritoMapper.toCarrito(carritoUpdateDto, carritoActual);
        return carritoMapper.toCarritoResponseDto(carritoRepository.save(carritoActualizado));
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Borrando Carrito por id: " + id);
        if(!carritoRepository.existsById(id)){
            throw new CarritoNotFoundException(id);
        }
        carritoRepository.deleteById(id);

    }

    @Override
    @Transactional
    public void deleteItemFromCarrito(Long carritoId, Long id) {
        log.info("Borrando plato del carrito con id:{}: " , id);

        Carrito_item itemDelete = carritoItemRepository.findById(id).orElseThrow(() -> new PlatoNotFoundException(id));

        if (!itemDelete.getCarrito().getId().equals(carritoId)) {
            throw new CarritoNotFoundException(carritoId);
        }

        carritoItemRepository.delete(itemDelete);
    }

}
