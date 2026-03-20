package es.pcomida.proyectocomida.Pedido.services;

import es.pcomida.proyectocomida.Pedido.dto.PedidoResponseDto;
import es.pcomida.proyectocomida.Pedido.dto.PedidoUpdateDto;
import es.pcomida.proyectocomida.Pedido.models.Estado;
import es.pcomida.proyectocomida.Pedido.dto.PedidoCreateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PedidosService {
    Page<PedidoResponseDto> findAll(Optional<Long> usuario, Optional<Estado> estado, Pageable pageable);

    PedidoResponseDto findById(Long id);

    PedidoResponseDto save(PedidoCreateDto pedidoCreateDto);

    PedidoResponseDto update(Long id, PedidoUpdateDto pedidoUpdateDto);

    void deleteById(Long id);
}
