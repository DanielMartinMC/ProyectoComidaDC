package es.pcomida.proyectocomida.plato.controller;

import es.pcomida.proyectocomida.plato.dto.PlatoCreateDto;
import es.pcomida.proyectocomida.plato.dto.PlatoResponseDto;
import es.pcomida.proyectocomida.plato.dto.PlatoUpdateDto;
import es.pcomida.proyectocomida.plato.models.Tipo;
import es.pcomida.proyectocomida.plato.services.PlatosService;
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
@RequestMapping("${api.version}/platos")
public class PlatoRestController {

    private final PlatosService platosService;
    private final PaginationLinksUtils paginationLinksUtils;

    @GetMapping
    public ResponseEntity<PageResponse<PlatoResponseDto>> getAll(
            @RequestParam(required = false) Optional<String> nombre,
            @RequestParam(required = false) Optional<Tipo> tipo,
            @RequestParam(required = false) Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Buscando todos los platos con filtros");
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(request.getRequestURL().toString());

        Page<PlatoResponseDto> pageResult = platosService.findAll(nombre, tipo, isDeleted, pageable);

        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatoResponseDto> getById(@PathVariable Long id) {
        log.info("Buscando plato por id: {}", id);
        return ResponseEntity.ok(platosService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PlatoResponseDto> save(@Valid @RequestBody PlatoCreateDto platoCreateDto) {
        log.info("Guardando nuevo plato: {}", platoCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(platosService.save(platoCreateDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatoResponseDto> update(@PathVariable Long id, @Valid @RequestBody PlatoUpdateDto platoUpdateDto) {
        log.info("Actualizando plato con id: {}", id);
        return ResponseEntity.ok(platosService.update(id, platoUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Eliminando plato con id: {}", id);
        platosService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
