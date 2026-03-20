package es.pcomida.proyectocomida.Carrito.mapper;

import es.pcomida.proyectocomida.Carrito.dto.CarritoCreateDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoResponseDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoUpdateDto;
import es.pcomida.proyectocomida.Carrito.models.Carrito;
import es.pcomida.proyectocomida.Carrito.models.Estados;
import es.pcomida.proyectocomida.Carrito_item.dto.CarritoItemResponseDTO;
import es.pcomida.proyectocomida.Usuario.models.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarritoMapper {

    public Carrito toCarrito(CarritoCreateDto carritoCreateDto, Usuario usuario) {
        return Carrito.builder()
                .usuario(usuario)
                .estado(Estados.Vacio)
                .items(new ArrayList<>())
                .codigoCupon(null)
                .descuento(0.0)
                .impuestosCalc(0.0)
                .total(0.0)
                .isDeleted(false)
                .build();
    }

    public Carrito toCarrito(CarritoUpdateDto carritoUpdateDto, Carrito carrito) {
        // Solo permitimos actualizar el código del cupón directamente
        if (carritoUpdateDto.getCodigoCupon() != null) {
            carrito.setCodigoCupon(carritoUpdateDto.getCodigoCupon());
        }
        return carrito;
    }

    public CarritoResponseDto toCarritoResponseDto(Carrito carrito) {
        if (carrito == null) return null;

        List<CarritoItemResponseDTO> itemDtos = carrito.getItems().stream()
                .map(item -> CarritoItemResponseDTO.builder()
                        .id(item.getId())
                        .platoID(item.getPlato().getId())
                        .nombrePlato(item.getPlato().getNombre())
                        .cantidad(item.getCantidad())
                        .precioUnidad(item.getPlato().getPrecio())
                        .subtotal(item.getPlato().getPrecio() * item.getCantidad())
                        .build())
                .toList();

        return CarritoResponseDto.builder()
                .id(carrito.getId())
                .usuarioId(carrito.getUsuario().getId())
                .items(itemDtos)
                .estado(carrito.getEstado())
                .codigoCupon(carrito.getCodigoCupon())
                .descuento(carrito.getDescuento())
                .impuestosCalc(carrito.getImpuestosCalc())
                .total(carrito.getTotal())
                .fechaCreacion(carrito.getFechaCreación())
                .fechaActualizacion(carrito.getFechaActualizacion())
                .build();
    }

    public List<CarritoResponseDto> toResponseDtoList(List<Carrito> carritos) {
        return carritos.stream()
                .map(this::toCarritoResponseDto)
                .toList();
    }
}
