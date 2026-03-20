package es.pcomida.proyectocomida.Usuario.controller;

import es.pcomida.proyectocomida.Usuario.dto.UsuarioCreateDTO;
import es.pcomida.proyectocomida.Usuario.dto.UsuarioResponseDTO;
import es.pcomida.proyectocomida.Usuario.dto.UsuarioUpdateDTO;
import es.pcomida.proyectocomida.Usuario.services.UsuarioService;
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
@RequestMapping("${api.version}/Usuario")
public class UsuarioRestController {
    private final UsuarioService usuarioService;
    private final PaginationLinksUtils paginationLinksUtils;

    @GetMapping
    public ResponseEntity<PageResponse<UsuarioResponseDTO>> getAll(
            @RequestParam(required = false) Optional<String> username,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Buscando todos los usuarios con username: {}, email: {} e isDeleted: {}", username, email, isDeleted);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(request.getRequestURL().toString());
        Page<UsuarioResponseDTO> pageResult = usuarioService.findAll(username, email, isDeleted, pageable);
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable Long id) {
        log.info("Buscando usuario por id: {}", id);
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> save(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        log.info("Guardando nuevo usuario: {}", usuarioCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(usuarioCreateDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        log.info("Actualizando usuario con id: {}", id);
        return ResponseEntity.ok(usuarioService.update(id, usuarioUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Eliminando usuario con id: {}", id);
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
