package es.pcomida.proyectocomida.reparto.controller;

import es.pcomida.proyectocomida.reparto.dto.RepartoResponseDto;
import es.pcomida.proyectocomida.reparto.models.RepartoEstado;
import es.pcomida.proyectocomida.reparto.services.RepartoService;
import es.pcomida.proyectocomida.utils.pagination.PageResponse;
import es.pcomida.proyectocomida.utils.pagination.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.version}/repartos") // En plural y minúscula por convención REST
public class RepartoRestController {

    private final RepartoService repartoService;
    private final PaginationLinksUtils paginationLinksUtils;

    // 1. Obtener todos los repartos (Para Administradores y Repartidores)
    @GetMapping
    public ResponseEntity<PageResponse<RepartoResponseDto>> getAll(
            @RequestParam(required = false) Optional<RepartoEstado> estado,
            @RequestParam(required = false) Optional<String> ciudad,
            @RequestParam(required = false) Optional<Long> repartidor_id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Buscando todos los repartos con filtros");
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(request.getRequestURL().toString());
        
        Page<RepartoResponseDto> pageResult = repartoService.findAll(estado, ciudad, repartidor_id, pageable);
        
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    // 2. Obtener un detalle (Para el Repartidor que va de camino)
    @GetMapping("/{id}")
    public ResponseEntity<RepartoResponseDto> getById(@PathVariable Long id) {
        log.info("Buscando reparto por id: {}", id);
        return ResponseEntity.ok(repartoService.findById(id));
    }

    // 3. Asignar un Repartidor (Lo hace el Administrador)
    @PatchMapping("/{id}/asignar/{repartidorId}")
    public ResponseEntity<RepartoResponseDto> asignarRepartidor(@PathVariable Long id, @PathVariable Long repartidorId) {
        log.info("Asignando el repartidor {} al reparto {}", repartidorId, id);
        return ResponseEntity.ok(repartoService.asignarRepartidor(id, repartidorId));
    }

    // 4. Actualizar el estado del reparto (Lo hace el Repartidor en su app)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<RepartoResponseDto> actualizarEstado(@PathVariable Long id, @RequestParam RepartoEstado nuevoEstado) {
        log.info("Actualizando estado del reparto {} a {}", id, nuevoEstado);
        return ResponseEntity.ok(repartoService.actualizarEstado(id, nuevoEstado));
    }

    // 5. Obtener repartos de un repartidor
    @GetMapping("/repartidor/{repartidorId}")
    public ResponseEntity<PageResponse<RepartoResponseDto>> getByRepartidor(
            @PathVariable Long repartidorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Buscando repartos del repartidor: {}", repartidorId);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(request.getRequestURL().toString());
        
        Page<RepartoResponseDto> pageResult = repartoService.findRepartosByRepartidor(repartidorId, pageable);
        
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    // 6. Obtener repartos de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<PageResponse<RepartoResponseDto>> getByUsuario(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Buscando repartos del usuario: {}", usuarioId);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(request.getRequestURL().toString());
        
        Page<RepartoResponseDto> pageResult = repartoService.findRepartosByUsuario(usuarioId, pageable);
        
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }
}
