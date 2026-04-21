package es.pcomida.proyectocomida.carrito.dto;

import es.pcomida.proyectocomida.carrito.models.Estados;
import es.pcomida.proyectocomida.carritoitem.dto.CarritoItemResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarritoResponseDTO {
    private Long id;
    private Long usuarioId;
    private List<CarritoItemResponseDTO> carritoItems;
    private Integer totalItems;
    private Double subtotal;
    private Double total;
    private Estados estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String codigoCupon;
    private Double descuento;
    private Double impuestos;
}
