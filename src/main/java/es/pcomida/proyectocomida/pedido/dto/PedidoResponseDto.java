package es.pcomida.proyectocomida.pedido.dto;

import es.pcomida.proyectocomida.pedido.models.Estado;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Builder
@Data
public class PedidoResponseDto {
    private Long id;
    private Long usuarioId;
    private Date fechaPedido;
    private Estado estado;
    private Double total;
    private List<PedidoItemResponseDto> pedidoItems;
    private String direccion;
    private Long metodoPagoId;
}