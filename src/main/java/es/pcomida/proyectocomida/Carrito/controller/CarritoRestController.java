package es.pcomida.proyectocomida.Carrito.controller;

import es.pcomida.proyectocomida.Carrito.dto.CarritoCreateDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoResponseDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoUpdateDto;
import es.pcomida.proyectocomida.Carrito.models.Estados;
import es.pcomida.proyectocomida.Carrito.services.CarritoService;
import es.pcomida.proyectocomida.Carrito_item.dto.AddCarritoItemDTO;
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

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.version}/Carrito")
public class CarritoRestController {
    private final CarritoService carritoService;
    private final PaginationLinksUtils paginationLinksUtils;

    @GetMapping
    public ResponseEntity<PageResponse<CarritoResponseDto>> findAll(
            @RequestParam(required = false) Optional<Long> usuarioId,
            @RequestParam(required = false) Optional<Estados> estado,
            @RequestParam(required = false) Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Buscando carritos con filtros");
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(request.getRequestURL().toString());

        Page<CarritoResponseDto> pageResult = carritoService.findAll(usuarioId, estado, isDeleted, pageable);

        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDto> findById(@PathVariable Long id) {
        log.info("Buscando Carrito por id={}", id);
        return ResponseEntity.ok(carritoService.findById(id));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<CarritoResponseDto> addPlato(@PathVariable Long id,
                                                       @Valid @RequestBody AddCarritoItemDTO itemDTO) {
        log.info("Añadiendo plato: {}, al carrito: {}", itemDTO, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.addPlatoToCarrito(id, itemDTO));
    }

    @PostMapping
    public ResponseEntity<CarritoResponseDto> create(@Valid @RequestBody CarritoCreateDto carritoCreateDto) {
        log.info("Creando Carrito : {}", carritoCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.save(carritoCreateDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarritoResponseDto> update(@PathVariable Long id, @Valid @RequestBody CarritoUpdateDto carritoUpdateDto) {
        log.info("Actualizando Carrito id={} con Carrito={}", id, carritoUpdateDto);
        return ResponseEntity.ok(carritoService.update(id, carritoUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Borrando Carrito por id: {}", id);
        carritoService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{carritoId}/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long carritoId, @PathVariable Long itemId) {
        log.info("Borrando el plato: {} del carrito: {}", itemId, carritoId);
        carritoService.deleteItemFromCarrito(carritoId, itemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
