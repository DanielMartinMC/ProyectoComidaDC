package es.pcomida.proyectocomida.Carrito.dto;

import es.pcomida.proyectocomida.Carrito.models.Estados;
import es.pcomida.proyectocomida.Plato.models.Plato;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class CarritoUpdateDto {
    private UUID usuario;
    private Estados estado;
    private LocalDateTime fechaCreación;
    private Float cupon;
    private Float descuento;
}
