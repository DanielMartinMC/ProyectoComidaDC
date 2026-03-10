package es.pcomida.proyectocomida.Carrito.services;

import es.pcomida.proyectocomida.Carrito.dto.CarritoCreateDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoResponseDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoUpdateDto;
import es.pcomida.proyectocomida.Carrito.models.Estados;
import es.pcomida.proyectocomida.Carrito_item.dto.AddCarritoItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CarritoService {
    Page<CarritoResponseDto> findAll(Optional<Long> usuarioId, Optional<Estados> estado, Optional<Boolean> isDeleted, Pageable pageable);

    CarritoResponseDto findById(Long id);

    CarritoResponseDto addPlatoToCarrito(Long carritoId, AddCarritoItemDTO itemDTO);

    CarritoResponseDto save(CarritoCreateDto carritoCreateDto);

    CarritoResponseDto update(Long id, CarritoUpdateDto carritoUpdateDto);

    void deleteItemFromCarrito(Long carritoId, Long id);
    void deleteById(Long id);
}
