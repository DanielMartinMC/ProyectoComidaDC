package es.pcomida.proyectocomida.transaccionpago.dto;

import es.pcomida.proyectocomida.transaccionpago.models.MetodoPago;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransaccionPagoCreateDto {
    @NotNull(message = "El ID del pedido no puede ser nulo")
    private Long pedidoId;

    @NotNull(message = "El método de pago no puede ser nulo")
    private MetodoPago metodoPago;

    @NotNull(message = "El importe no puede ser nulo")
    @Positive(message = "El importe debe ser positivo")
    private BigDecimal amount;

    @NotNull(message = "La moneda no puede ser nula")
    private String currency;
}
