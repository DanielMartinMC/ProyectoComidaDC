package es.pcomida.proyectocomida.Carrito.mapper;

import es.pcomida.proyectocomida.Carrito.dto.CarritoCreateDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoResponseDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoUpdateDto;
import es.pcomida.proyectocomida.Carrito.models.Carrito;
import es.pcomida.proyectocomida.Carrito_item.dto.CarritoDTO;
import es.pcomida.proyectocomida.Carrito_item.dto.CarritoItemDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarritoMapper {
    public Carrito toCarrito(CarritoCreateDto carritoCreateDto){
        return Carrito.builder()
                .usuario(carritoCreateDto.getUsuario())
                .estado(carritoCreateDto.getEstado())
                .items(new ArrayList<>())
                .cupon(carritoCreateDto.getCupon())
                .descuento(carritoCreateDto.getDescuento())
                .build();
    }

    public CarritoResponseDto toCarritoResponseDto(Carrito carrito){
        List<CarritoItemDTO> itemDtos = carrito.getItems().stream()
                .map(item -> CarritoItemDTO.builder()
                        .platoID(item.getPlato().getId())
                        .nombrePlato(item.getPlato().getNombre())
                        .cantidad(item.getCantidad())
                        .precioUnidad(item.getPlato().getPrecio())
                        .subtotal(item.getPlato().getPrecio() * item.getCantidad())
                        .build())
                .toList();


        return CarritoResponseDto.builder()
                .id(carrito.getId())
                .usuario(carrito.getUsuario())
                .items(itemDtos)
                .estado(carrito.getEstado())
                .total(itemDtos.stream().mapToDouble(item -> item.getSubtotal()).sum())
                .build();

    }
}
