package es.pcomida.proyectocomida.carrito.services;

import es.pcomida.proyectocomida.carrito.dto.CarritoCreateDto;
import es.pcomida.proyectocomida.carrito.dto.CarritoResponseDTO;
import es.pcomida.proyectocomida.carrito.dto.CarritoUpdateDto;
import es.pcomida.proyectocomida.carrito.models.Estados;
import es.pcomida.proyectocomida.carritoitem.dto.AddCarritoItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CarritoService {
    Page<CarritoResponseDTO> findAll(Optional<Long> usuarioId, Optional<Estados> estado, Optional<Boolean> isDeleted, Pageable pageable);

    CarritoResponseDTO findById(Long id);

    CarritoResponseDTO addPlatoToCarrito(Long carritoId, AddCarritoItemDTO itemDTO);

    CarritoResponseDTO save(CarritoCreateDto carritoCreateDto);

    CarritoResponseDTO update(Long id, CarritoUpdateDto carritoUpdateDto);

    void deleteItemFromCarrito(Long carritoId, Long id);
    void deleteById(Long id);
}
