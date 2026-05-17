package es.pcomida.proyectocomida.pedido.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PedidoItemResponseDto {
    private Long platoId;
    private String nombrePlato;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
