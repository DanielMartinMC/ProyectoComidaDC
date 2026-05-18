package es.pcomida.proyectocomida.pedido.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequestDto {
    @NotNull
    private Long carritoId;

    @NotNull
    private Long metodoPagoId;
}