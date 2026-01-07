package es.pcomida.proyectocomida.Carrito.services;


import es.pcomida.proyectocomida.Carrito.dto.CarritoCreateDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoResponseDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoUpdateDto;
import es.pcomida.proyectocomida.Carrito.excepctions.CarritoNotFoundException;
import es.pcomida.proyectocomida.Carrito.mapper.CarritoMapper;
import es.pcomida.proyectocomida.Carrito.models.Carrito;
import es.pcomida.proyectocomida.Carrito.models.Estados;
import es.pcomida.proyectocomida.Carrito.repositories.CarritoRepository;
import es.pcomida.proyectocomida.Plato.models.Tipo;
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
    private final CarritoService carritoService;

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
        var carrito = carritoService.findById(id);
        carritoRepository.deleteById(id);

    }
}
