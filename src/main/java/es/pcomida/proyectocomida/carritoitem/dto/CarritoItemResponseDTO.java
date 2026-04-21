package es.pcomida.proyectocomida.carritoitem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarritoItemResponseDTO {
    private Long id;
    private Long platoID;
    private String nombrePlato;
    private Double precioUnidad;
    private Integer cantidad;
    private Double subtotal;
}
