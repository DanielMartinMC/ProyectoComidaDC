package es.pcomida.proyectocomida.pedido.dto;

import es.pcomida.proyectocomida.pedido.models.Estado;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class PedidoCreateDto {
    private Long id;
    private Date fechaPedido;
    private Estado estado;
    private Double total;
    private Long usuario;
    private String direccion;
}
