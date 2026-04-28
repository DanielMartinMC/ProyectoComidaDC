package es.pcomida.proyectocomida.usuario.controller;

import es.pcomida.proyectocomida.usuario.dto.UsuarioResponseDTO;
import es.pcomida.proyectocomida.usuario.dto.UsuarioUpdateDTO;
import es.pcomida.proyectocomida.usuario.services.UsuarioService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import es.pcomida.proyectocomida.usuario.models.Usuario;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.version}/usuarios")
public class UsuarioRestController {
    private final UsuarioService usuarioService;
    private final PaginationLinksUtils paginationLinksUtils;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
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
        log.info("Buscando todos los usuarios con filtros");
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(request.getRequestURL().toString());
        Page<UsuarioResponseDTO> pageResult = usuarioService.findAll(username, email, isDeleted, pageable);
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioResponseDTO> getMyProfile(@AuthenticationPrincipal Usuario usuario) {
        log.info("Obteniendo perfil del usuario: {}", usuario.getUsername());
        return ResponseEntity.ok(usuarioService.findById(usuario.getId()));
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioResponseDTO> updateMyProfile(@AuthenticationPrincipal Usuario usuario, @Valid @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        log.info("Actualizando perfil del usuario: {}", usuario.getUsername());
        return ResponseEntity.ok(usuarioService.update(usuario.getId(), usuarioUpdateDTO));
    }

    @PostMapping("/me/suscribir")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UsuarioResponseDTO> subscribe(@AuthenticationPrincipal Usuario usuario) {
        log.info("Suscribiendo al usuario: {}", usuario.getUsername());
        return ResponseEntity.ok(usuarioService.subscribe(usuario.getId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable Long id) {
        log.info("Buscando usuario por id: {}", id);
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        log.info("Actualizando usuario con id: {}", id);
        return ResponseEntity.ok(usuarioService.update(id, usuarioUpdateDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Eliminando usuario con id: {}", id);
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
