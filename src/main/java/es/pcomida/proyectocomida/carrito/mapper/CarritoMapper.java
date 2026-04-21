package es.pcomida.proyectocomida.carrito.mapper;

import es.pcomida.proyectocomida.carrito.dto.CarritoCreateDto;
import es.pcomida.proyectocomida.carrito.dto.CarritoResponseDTO;
import es.pcomida.proyectocomida.carrito.dto.CarritoUpdateDto;
import es.pcomida.proyectocomida.carrito.models.Carrito;
import es.pcomida.proyectocomida.carrito.models.Estados;
import es.pcomida.proyectocomida.carritoitem.dto.CarritoItemResponseDTO;
import es.pcomida.proyectocomida.usuario.models.Usuario;
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

    public CarritoResponseDTO toCarritoResponseDTO(Carrito carrito) {
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

        int totalItems = itemDtos.stream().mapToInt(CarritoItemResponseDTO::getCantidad).sum();
        double subtotal = itemDtos.stream().mapToDouble(CarritoItemResponseDTO::getSubtotal).sum();

        return CarritoResponseDTO.builder()
                .id(carrito.getId())
                .usuarioId(carrito.getUsuario().getId())
                .carritoItems(itemDtos)
                .totalItems(totalItems)
                .subtotal(subtotal)
                .total(carrito.getTotal())
                .estado(carrito.getEstado())
                .fechaCreacion(carrito.getFechaCreación())
                .fechaActualizacion(carrito.getFechaActualizacion())
                .codigoCupon(carrito.getCodigoCupon())
                .descuento(carrito.getDescuento())
                .impuestos(carrito.getImpuestosCalc())
                .build();
    }

    public List<CarritoResponseDTO> toResponseDtoList(List<Carrito> carritos) {
        return carritos.stream()
                .map(this::toCarritoResponseDTO)
                .toList();
    }
}
