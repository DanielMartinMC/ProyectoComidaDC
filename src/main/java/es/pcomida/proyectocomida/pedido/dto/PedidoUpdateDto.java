package es.pcomida.proyectocomida.pedido.dto;

import es.pcomida.proyectocomida.pedido.models.Estado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoUpdateDto {
    private Estado estado;
    private String direccion;
}
