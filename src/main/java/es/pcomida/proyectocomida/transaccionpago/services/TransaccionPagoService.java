package es.pcomida.proyectocomida.transaccionpago.services;

import es.pcomida.proyectocomida.transaccionpago.dto.TransaccionPagoCreateDto;
import es.pcomida.proyectocomida.transaccionpago.dto.TransaccionPagoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransaccionPagoService {
    Page<TransaccionPagoResponseDto> findAll(Pageable pageable);
    TransaccionPagoResponseDto findById(Long id);
    TransaccionPagoResponseDto findByPedidoId(Long pedidoId);
    TransaccionPagoResponseDto create(TransaccionPagoCreateDto createDto);
    // Aquí irían métodos para procesar el pago, cancelarlo, etc.
}
