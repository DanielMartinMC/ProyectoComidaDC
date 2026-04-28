package es.pcomida.proyectocomida.metodopago.controller;

import es.pcomida.proyectocomida.metodopago.dto.MetodoPagoCreateDto;
import es.pcomida.proyectocomida.metodopago.dto.MetodoPagoDto;
import es.pcomida.proyectocomida.metodopago.dto.MetodoPagoUpdateDto;
import es.pcomida.proyectocomida.metodopago.services.MetodoPagoService;
import es.pcomida.proyectocomida.usuario.models.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.version}/metodos-pago")
@RequiredArgsConstructor
public class MetodoPagoRestController {

    private final MetodoPagoService metodoPagoService;

    @GetMapping
    public ResponseEntity<List<MetodoPagoDto>> getMetodosPago(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(metodoPagoService.getMetodosPago(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoPagoDto> getMetodoPagoById(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(metodoPagoService.getMetodoPagoById(id, usuario));
    }

    @PostMapping
    public ResponseEntity<MetodoPagoDto> createMetodoPago(@Valid @RequestBody MetodoPagoCreateDto metodoPagoDto, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metodoPagoService.createMetodoPago(metodoPagoDto, usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetodoPagoDto> updateMetodoPago(@PathVariable Long id, @Valid @RequestBody MetodoPagoUpdateDto metodoPagoUpdateDto, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(metodoPagoService.updateMetodoPago(id, metodoPagoUpdateDto, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetodoPago(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        metodoPagoService.deleteMetodoPago(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/default")
    public ResponseEntity<Void> setDefaultMetodoPago(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        metodoPagoService.setDefaultMetodoPago(id, usuario);
        return ResponseEntity.ok().build();
    }
}
