package es.pcomida.proyectocomida.pedido.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutRequestDto {
    @NotNull(message = "El id del carrito no puede ser nulo")
    private Long carritoId;
}
