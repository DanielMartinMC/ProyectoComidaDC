package es.pcomida.proyectocomida.pedido.mapper;

import es.pcomida.proyectocomida.carrito.models.Carrito;
import es.pcomida.proyectocomida.metodopago.models.MetodoPago;
import es.pcomida.proyectocomida.pedido.dto.PedidoItemResponseDto;
import es.pcomida.proyectocomida.pedido.dto.PedidoResponseDto;
import es.pcomida.proyectocomida.pedido.dto.PedidoUpdateDto;
import es.pcomida.proyectocomida.pedido.models.Pedido;
import es.pcomida.proyectocomida.pedido.models.PedidoItem;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    public Pedido toPedido(Carrito carrito, MetodoPago metodoPago) {
        if (carrito == null) {
            return null;
        }

        Pedido pedido = Pedido.builder()
                .usuario(carrito.getUsuario())
                .direccion(carrito.getUsuario().getDireccion())
                .total(carrito.getTotal())
                .fechaPedido(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .metodoPago(metodoPago)
                .build();

        List<PedidoItem> pedidoItems = carrito.getItems().stream()
                .map(carritoItem -> PedidoItem.builder()
                        .plato(carritoItem.getPlato())
                        .cantidad(carritoItem.getCantidad())
                        .precioUnitario(carritoItem.getPrecioUnitario())
                        .pedido(pedido)
                        .build())
                .collect(Collectors.toList());

        pedido.setPedidoItems(pedidoItems);
        return pedido;
    }

    public Pedido toPedido(PedidoUpdateDto pedidoUpdateDto, Pedido pedido) {
        if (pedidoUpdateDto.getEstado() != null) {
            pedido.setEstado(pedidoUpdateDto.getEstado());
        }
        if (pedidoUpdateDto.getDireccion() != null) {
            pedido.setDireccion(pedidoUpdateDto.getDireccion());
        }
        return pedido;
    }

    public PedidoResponseDto toPedidoResponseDto(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        List<PedidoItemResponseDto> itemDtos = pedido.getPedidoItems().stream()
                .map(item -> PedidoItemResponseDto.builder()
                        .platoId(item.getPlato().getId())
                        .nombrePlato(item.getPlato().getNombre())
                        .cantidad(item.getCantidad())
                        .precioUnitario(item.getPrecioUnitario())
                        .subtotal(item.getPrecioUnitario() * item.getCantidad())
                        .build())
                .collect(Collectors.toList());

        return PedidoResponseDto.builder()
                .id(pedido.getId())
                .usuarioId(pedido.getUsuario() != null ? pedido.getUsuario().getId() : null)
                .fechaPedido(pedido.getFechaPedido())
                .estado(pedido.getEstado())
                .total(pedido.getTotal())
                .pedidoItems(itemDtos)
                .direccion(pedido.getDireccion())
                .metodoPagoId(pedido.getMetodoPago() != null ? pedido.getMetodoPago().getId() : null)
                .build();
    }

    public List<PedidoResponseDto> toResponseDtoList(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(this::toPedidoResponseDto)
                .collect(Collectors.toList());
    }
}