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
public class CarritoCreateDto {
    private UUID usuario;
    private Estados estado;
    private List<Plato> platos;
    private Float cupon;
    private Float descuento;
}
