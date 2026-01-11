package es.pcomida.proyectocomida.Carrito.mapper;

import es.pcomida.proyectocomida.Carrito.dto.CarritoCreateDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoResponseDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoUpdateDto;
import es.pcomida.proyectocomida.Carrito.models.Carrito;
import es.pcomida.proyectocomida.Carrito_item.dto.CarritoItemDTO;
import es.pcomida.proyectocomida.Carrito_item.models.Carrito_item;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarritoMapper {
    public Carrito toCarrito(CarritoCreateDto carritoCreateDto){
        return Carrito.builder()
                .usuario(carritoCreateDto.getUsuario())
                .estado(carritoCreateDto.getEstado())
                .items(new ArrayList<>())
                .cupon(carritoCreateDto.getCupon())
                .descuento(carritoCreateDto.getDescuento())
                .ImpuestosCalc(0.0f)
                .build();
    }
    public Carrito toCarrito(CarritoUpdateDto carritoUpdateDto,Carrito carrito){
        return Carrito.builder()
                .id(carrito.getId())
                .usuario(carritoUpdateDto.getUsuario() != null ? carritoUpdateDto.getUsuario() : carrito.getUsuario())
                .estado(carritoUpdateDto.getEstado() != null ? carritoUpdateDto.getEstado() : carrito.getEstado())
                .fechaCreación(carrito.getFechaCreación())
                .items(carrito.getItems())
                .cupon(carritoUpdateDto.getCupon() != null ? carritoUpdateDto.getCupon() : carrito.getCupon())
                .descuento(carritoUpdateDto.getDescuento() != null ? carritoUpdateDto.getDescuento() : carrito.getDescuento())
                .ImpuestosCalc(carrito.getImpuestosCalc())
                .build();
    }

    public CarritoResponseDto toCarritoResponseDto(Carrito carrito){
        if(carrito == null){ return null;}

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

    private CarritoItemDTO toItemDTO(Carrito_item item) {
        return CarritoItemDTO.builder()
                .id(item.getId())
                .platoID(item.getPlato().getId())
                .nombrePlato(item.getPlato().getNombre())
                .precioUnidad(item.getPlato().getPrecio())
                .cantidad(item.getCantidad())
                .subtotal(item.getPlato().getPrecio() * item.getCantidad())
                .build();
    }

    public List<CarritoResponseDto> toResponseDtoList(List<Carrito> carritos) {
        return carritos.stream()
                .map(this::toCarritoResponseDto)
                .toList();
    }
}
