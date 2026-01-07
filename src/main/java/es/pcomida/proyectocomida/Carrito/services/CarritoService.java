package es.pcomida.proyectocomida.Carrito.services;

import es.pcomida.proyectocomida.Carrito.dto.CarritoCreateDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoResponseDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoUpdateDto;
import es.pcomida.proyectocomida.Carrito.models.Carrito;
import es.pcomida.proyectocomida.Carrito.models.Estados;
import es.pcomida.proyectocomida.Plato.models.Tipo;

import java.util.List;
import java.util.UUID;

public interface CarritoService {
    List<CarritoResponseDto> findAll(UUID usuario, Estados estados);

    CarritoResponseDto findById(Long id);



    CarritoResponseDto save(CarritoCreateDto carritoCreateDto);

    CarritoResponseDto update(Long id, CarritoUpdateDto carritoUpdateDto);

    void deleteById(Long id);
}
