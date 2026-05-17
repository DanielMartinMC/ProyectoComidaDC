package es.pcomida.proyectocomida.pedido.dto;

import es.pcomida.proyectocomida.pedido.models.Estado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoUpdateDto {

    private Long id;
    private Date fechaPedido;
    private Estado estado;
    private Double total;
    private String direccion;
}