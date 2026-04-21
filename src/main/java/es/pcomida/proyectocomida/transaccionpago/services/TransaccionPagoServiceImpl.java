package es.pcomida.proyectocomida.transaccionpago.services;

import es.pcomida.proyectocomida.pedido.exceptions.PedidoNotFoundException;
import es.pcomida.proyectocomida.pedido.models.Pedido;
import es.pcomida.proyectocomida.pedido.repositories.PedidosRepository;
import es.pcomida.proyectocomida.transaccionpago.dto.TransaccionPagoCreateDto;
import es.pcomida.proyectocomida.transaccionpago.dto.TransaccionPagoResponseDto;
import es.pcomida.proyectocomida.transaccionpago.exceptions.TransaccionNotFoundException;
import es.pcomida.proyectocomida.transaccionpago.mapper.TransaccionPagoMapper;
import es.pcomida.proyectocomida.transaccionpago.models.TransaccionPago;
import es.pcomida.proyectocomida.transaccionpago.repositories.TransaccionPagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransaccionPagoServiceImpl implements TransaccionPagoService {

    private final TransaccionPagoRepository transaccionRepository;
    private final PedidosRepository pedidosRepository;
    private final TransaccionPagoMapper transaccionMapper;

    @Override
    public Page<TransaccionPagoResponseDto> findAll(Pageable pageable) {
        log.info("Buscando todas las transacciones");
        return transaccionRepository.findAll(pageable)
                .map(transaccionMapper::toTransaccionPagoResponseDto);
    }

    @Override
    public TransaccionPagoResponseDto findById(Long id) {
        log.info("Buscando transacción por id: {}", id);
        return transaccionRepository.findById(id)
                .map(transaccionMapper::toTransaccionPagoResponseDto)
                .orElseThrow(() -> new TransaccionNotFoundException(id));
    }

    @Override
    public TransaccionPagoResponseDto findByPedidoId(Long pedidoId) {
        log.info("Buscando transacción por id de pedido: {}", pedidoId);
        return transaccionRepository.findByPedidoId(pedidoId)
                .map(transaccionMapper::toTransaccionPagoResponseDto)
                .orElseThrow(() -> new TransaccionNotFoundException("No se encontró transacción para el pedido con id: " + pedidoId));
    }

    @Override
    @Transactional
    public TransaccionPagoResponseDto create(TransaccionPagoCreateDto createDto) {
        log.info("Creando nueva transacción para el pedido: {}", createDto.getPedidoId());

        Pedido pedido = pedidosRepository.findById(createDto.getPedidoId())
                .orElseThrow(() -> new PedidoNotFoundException(createDto.getPedidoId()));

        // Aquí podríamos añadir lógica para verificar que el pedido no tenga ya una transacción, etc.

        TransaccionPago transaccion = transaccionMapper.toTransaccionPago(createDto, pedido);
        TransaccionPago savedTransaccion = transaccionRepository.save(transaccion);

        return transaccionMapper.toTransaccionPagoResponseDto(savedTransaccion);
    }
}
