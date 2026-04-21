package es.pcomida.proyectocomida.transaccionpago.mapper;

import es.pcomida.proyectocomida.pedido.models.Pedido;
import es.pcomida.proyectocomida.transaccionpago.dto.TransaccionPagoCreateDto;
import es.pcomida.proyectocomida.transaccionpago.dto.TransaccionPagoResponseDto;
import es.pcomida.proyectocomida.transaccionpago.models.EstadoPago;
import es.pcomida.proyectocomida.transaccionpago.models.TransaccionPago;
import org.springframework.stereotype.Component;

@Component
public class TransaccionPagoMapper {

    public TransaccionPago toTransaccionPago(TransaccionPagoCreateDto dto, Pedido pedido) {
        return TransaccionPago.builder()
                .pedido(pedido)
                .metodoPago(dto.getMetodoPago())
                .amount(dto.getAmount())
                .currency(dto.getCurrency())
                .status(EstadoPago.PENDIENTE) // El estado inicial es siempre PENDIENTE
                .build();
    }

    public TransaccionPagoResponseDto toTransaccionPagoResponseDto(TransaccionPago transaccion) {
        return TransaccionPagoResponseDto.builder()
                .id(transaccion.getId())
                .pedidoId(transaccion.getPedido().getId())
                .metodoPago(transaccion.getMetodoPago())
                .amount(transaccion.getAmount())
                .currency(transaccion.getCurrency())
                .status(transaccion.getStatus())
                .paymentDetails(transaccion.getPaymentDetails())
                .confirmationDate(transaccion.getConfirmationDate())
                .build();
    }
}
