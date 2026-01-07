package es.pcomida.proyectocomida.Carrito.dto;

import es.pcomida.proyectocomida.Carrito.models.Estados;
import es.pcomida.proyectocomida.Carrito_item.dto.CarritoItemDTO;
import es.pcomida.proyectocomida.Carrito_item.models.Carrito_item;
import es.pcomida.proyectocomida.Plato.models.Plato;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private List<CarritoItemDTO> items;
    private Float cupon;
    private Float descuento;
    private Float ImpuestosCalc;
    private Double total;
}
