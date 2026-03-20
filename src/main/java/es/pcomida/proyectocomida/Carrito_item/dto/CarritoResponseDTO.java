package es.pcomida.proyectocomida.Carrito_item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarritoResponseDTO {
    private Long id;
    private Long usuarioId;
    private List<CarritoItemResponseDTO> carritoItems;
    private Double precioTotal;
}
