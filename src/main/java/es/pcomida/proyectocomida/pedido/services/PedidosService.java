package es.pcomida.proyectocomida.pedido.services;

import es.pcomida.proyectocomida.pedido.dto.CheckoutRequestDto;
import es.pcomida.proyectocomida.pedido.dto.PedidoResponseDto;
import es.pcomida.proyectocomida.pedido.dto.PedidoUpdateDto;
import es.pcomida.proyectocomida.pedido.models.Estado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Optional;

public interface PedidosService {
    Page<PedidoResponseDto> findAll(Optional<Long> usuario, Optional<Estado> estado, Optional<Date>fechaDesde, Optional<Date>fechaHasta , Pageable pageable);

    PedidoResponseDto findById(Long id);

    PedidoResponseDto save(CheckoutRequestDto checkoutRequestDto);

    PedidoResponseDto update(Long id, PedidoUpdateDto pedidoUpdateDto);

    void deleteById(Long id);
}
