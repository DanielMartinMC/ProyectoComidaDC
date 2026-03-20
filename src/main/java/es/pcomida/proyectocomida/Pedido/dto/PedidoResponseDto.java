package es.pcomida.proyectocomida.Pedido.dto;

import es.pcomida.proyectocomida.Pedido.models.Estado;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class PedidoResponseDto {
    private Long id;
    private Date fechaPedido;
    private Estado estado;
    private Double total;
    private Long usuario;
    private String direccion;

}
