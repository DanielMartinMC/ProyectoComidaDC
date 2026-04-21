package es.pcomida.proyectocomida.carrito.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarritoCreateDto {
    @NotNull(message = "El id de usuario no puede ser nulo")
    private Long usuarioId;
}
