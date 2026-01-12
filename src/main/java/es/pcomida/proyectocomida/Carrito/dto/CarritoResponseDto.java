package es.pcomida.proyectocomida.Carrito.dto;

import es.pcomida.proyectocomida.Carrito.models.Estados;
import es.pcomida.proyectocomida.Carrito_item.dto.CarritoItemResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoResponseDto {
    private Long id;
    private UUID usuario;
    private Estados estado;
    private List<CarritoItemResponseDTO> items;
    private Float cupon;
    private Float descuento;
    private Float ImpuestosCalc;
    private Double total;
}
