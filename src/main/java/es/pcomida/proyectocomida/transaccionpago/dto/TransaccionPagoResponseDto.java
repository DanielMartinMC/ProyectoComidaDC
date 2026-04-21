package es.pcomida.proyectocomida.transaccionpago.dto;

import es.pcomida.proyectocomida.transaccionpago.models.EstadoPago;
import es.pcomida.proyectocomida.transaccionpago.models.MetodoPago;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransaccionPagoResponseDto {
    private Long id;
    private Long pedidoId;
    private MetodoPago metodoPago;
    private BigDecimal amount;
    private String currency;
    private EstadoPago status;
    private String paymentDetails;
    private LocalDateTime confirmationDate;
}
