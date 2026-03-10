package es.pcomida.proyectocomida.Carrito.dto;

import es.pcomida.proyectocomida.Carrito.models.Estados;
import es.pcomida.proyectocomida.Carrito_item.dto.CarritoItemResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoResponseDto {
    private Long id;
    private Long usuarioId;
    private Estados estado;
    private List<CarritoItemResponseDTO> items;
    private String codigoCupon;
    private Double descuento;
    private Double impuestosCalc;
    private Double total;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
