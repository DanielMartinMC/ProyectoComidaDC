package es.pcomida.proyectocomida.pedido.controller;

import es.pcomida.proyectocomida.pedido.dto.PedidoCreateDto;
import es.pcomida.proyectocomida.pedido.dto.PedidoResponseDto;
import es.pcomida.proyectocomida.pedido.dto.PedidoUpdateDto;
import es.pcomida.proyectocomida.pedido.models.Estado;
import es.pcomida.proyectocomida.pedido.services.PedidosService;
import es.pcomida.proyectocomida.utils.pagination.PageResponse;
import es.pcomida.proyectocomida.utils.pagination.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController // Es un controlador Rest
@RequestMapping("api/${api.version}/pedidos") // Es la ruta del controlador (en plural minúscula por convención)
public class PedidoRestController {

    private final PedidosService pedidosService;
    private final PaginationLinksUtils paginationLinksUtils;

    @GetMapping
    public ResponseEntity<PageResponse<PedidoResponseDto>> getAll(
            @RequestParam(required = false) Optional<Long> usuario,
            @RequestParam(required = false) Optional<Estado> estado,
            @RequestParam(required = false) Optional<Date>fechaDesde,
            @RequestParam(required = false) Optional<Date>fechaHasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Buscando todos los pedidos con filtros");
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(request.getRequestURL().toString());

        Page<PedidoResponseDto> pageResult = pedidosService.findAll(usuario, estado,fechaDesde,fechaHasta ,pageable);

        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> getById(@PathVariable Long id) {
        log.info("Buscando pedido por id={}", id);
        return ResponseEntity.ok(pedidosService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDto> create(@Valid @RequestBody PedidoCreateDto pedidoCreateDto) {
        log.info("Creando pedido : {}", pedidoCreateDto);
        var saved = pedidosService.save(pedidoCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> update(@PathVariable Long id, @Valid @RequestBody PedidoUpdateDto pedidoUpdateDto) {
        log.info("Actualizando pedido id={} con datos={}", id, pedidoUpdateDto);
        return ResponseEntity.ok(pedidosService.update(id, pedidoUpdateDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> updatePartial(@PathVariable Long id, @Valid @RequestBody PedidoUpdateDto pedidoUpdateDto) {
        log.info("Actualizando parcialmente pedido con id={} con datos={}",id, pedidoUpdateDto);
        return ResponseEntity.ok(pedidosService.update(id, pedidoUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Borrando pedido por id: {}", id);
        pedidosService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
