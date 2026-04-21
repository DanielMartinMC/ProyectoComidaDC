package es.pcomida.proyectocomida.pedido.mapper;

import es.pcomida.proyectocomida.pedido.dto.PedidoCreateDto;
import es.pcomida.proyectocomida.pedido.dto.PedidoResponseDto;
import es.pcomida.proyectocomida.pedido.dto.PedidoUpdateDto;
import es.pcomida.proyectocomida.pedido.models.Pedido;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapper {
    public Pedido toPedido(PedidoCreateDto pedidoCreateDto) {
        return Pedido.builder()
                .id(pedidoCreateDto.getId())
                .usuario(pedidoCreateDto.getUsuario())
                .fechaPedido(pedidoCreateDto.getFechaPedido())
                .estado(pedidoCreateDto.getEstado())
                .total(pedidoCreateDto.getTotal())
                .direccion(pedidoCreateDto.getDireccion())
                .build();
    }

    public Pedido toPedido(PedidoUpdateDto pedidoUpdateDto, Pedido pedido) {
        return Pedido.builder()
                .id(pedido.getId())
                .usuario(pedidoUpdateDto.getUsuario() != null ? pedidoUpdateDto.getUsuario() : pedido.getUsuario())
                .fechaPedido(pedidoUpdateDto.getFechaPedido() != null ? pedidoUpdateDto.getFechaPedido() : pedido.getFechaPedido())
                .estado(pedidoUpdateDto.getEstado() != null ? pedidoUpdateDto.getEstado() : pedido.getEstado())
                .total(pedidoUpdateDto.getTotal() != null ? pedidoUpdateDto.getTotal() : pedido.getTotal())
                .direccion(pedidoUpdateDto.getDireccion() != null ? pedidoUpdateDto.getDireccion() : pedido.getDireccion())
                .build();
    }

    public PedidoResponseDto toPedidoResponseDto(Pedido pedido) {
        return PedidoResponseDto.builder()
                .id(pedido.getId())
                .usuario(pedido.getUsuario())
                .fechaPedido(pedido.getFechaPedido())
                .estado(pedido.getEstado())
                .total(pedido.getTotal())
                .direccion(pedido.getDireccion())
                .build();
    }

    // Mapeamos de modelo a DTO (lista)
    public List<PedidoResponseDto> toResponseDtoList(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(this::toPedidoResponseDto)
                .toList();
    }

}