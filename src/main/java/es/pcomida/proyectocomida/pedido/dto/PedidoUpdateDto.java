package es.pcomida.proyectocomida.pedido.dto;

import es.pcomida.proyectocomida.pedido.models.Estado;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class PedidoUpdateDto {

    private Long id;
    private Date fechaPedido;
    private Estado estado;
    private Double total;
    private Long usuario;
    private String direccion;
}
