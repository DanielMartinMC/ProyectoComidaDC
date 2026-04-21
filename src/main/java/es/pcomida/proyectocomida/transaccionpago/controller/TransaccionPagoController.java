package es.pcomida.proyectocomida.transaccionpago.controller;

import es.pcomida.proyectocomida.transaccionpago.dto.TransaccionPagoCreateDto;
import es.pcomida.proyectocomida.transaccionpago.dto.TransaccionPagoResponseDto;
import es.pcomida.proyectocomida.transaccionpago.services.TransaccionPagoService;
import es.pcomida.proyectocomida.utils.pagination.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.version}/transaccion-pago")
@Slf4j
@RequiredArgsConstructor
public class TransaccionPagoController {

    private final TransaccionPagoService transaccionService;

    @GetMapping
    public ResponseEntity<PageResponse<TransaccionPagoResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Buscando todas las transacciones");
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(transaccionService.findAll(pageable), sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransaccionPagoResponseDto> getById(@PathVariable Long id) {
        log.info("Buscando transacción por id: {}", id);
        return ResponseEntity.ok(transaccionService.findById(id));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<TransaccionPagoResponseDto> getByPedidoId(@PathVariable Long pedidoId) {
        log.info("Buscando transacción por id de pedido: {}", pedidoId);
        return ResponseEntity.ok(transaccionService.findByPedidoId(pedidoId));
    }

    @PostMapping
    public ResponseEntity<TransaccionPagoResponseDto> create(
            @Valid @RequestBody TransaccionPagoCreateDto createDto
    ) {
        log.info("Creando nueva transacción: {}", createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaccionService.create(createDto));
    }
}
