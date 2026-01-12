package es.pcomida.proyectocomida.Pedido.controller;

import es.pcomida.proyectocomida.Pedido.dto.PedidoCreateDto;
import es.pcomida.proyectocomida.Pedido.dto.PedidoResponseDto;
import es.pcomida.proyectocomida.Pedido.dto.PedidoUpdateDto;
import es.pcomida.proyectocomida.Pedido.models.Estado;
import es.pcomida.proyectocomida.Pedido.services.PedidosService;
import es.pcomida.proyectocomida.Plato.dto.PlatoCreateDto;
import es.pcomida.proyectocomida.Plato.dto.PlatoResponseDto;
import es.pcomida.proyectocomida.Plato.dto.PlatoUpdateDto;
import es.pcomida.proyectocomida.Plato.models.Tipo;
import es.pcomida.proyectocomida.Plato.services.PlatosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController // Es un controlador Rest
@RequestMapping("api/${api.version}/Pedido") // Es la ruta del controlador
public class PedidoRestController {

    private final PedidosService pedidosService;


    @GetMapping()
    public ResponseEntity<List<PedidoResponseDto>> getAll(@RequestParam(required = false) Long usuario,
                                                          @RequestParam(required = false) Estado estado) {
        log.info("Buscando pedidos por usuario={}, estado={}", usuario, estado);
        return ResponseEntity.ok(pedidosService.findAll(usuario, estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> getById(@PathVariable Long id) {
        log.info("Buscando pedido por id={}", id);
        return ResponseEntity.ok(pedidosService.findById(id));
    }


    @PostMapping()
    public ResponseEntity<PedidoResponseDto> create(@Valid @RequestBody PedidoCreateDto pedidoCreateDto) {
        log.info("Creando pedido : {}", pedidoCreateDto);
        var saved = pedidosService.save(pedidoCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> update(@PathVariable Long id, @Valid @RequestBody PedidoUpdateDto pedidoUpdateDto) {
        log.info("Actualizando tarjeta id={} con tarjeta={}", id, pedidoUpdateDto);
        return ResponseEntity.ok(pedidosService.update(id, pedidoUpdateDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> updatePartial(@PathVariable Long id, @Valid @RequestBody PedidoUpdateDto pedidoUpdateDto) {
        log.info("Actualizando parcialmente tarjeta con id={} con plato={}",id, pedidoUpdateDto);
        return ResponseEntity.ok(pedidosService.update(id, pedidoUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Borrando producto por id: {}", id);
        pedidosService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        BindingResult result = ex.getBindingResult();
        problemDetail.setDetail("Falló la validación para el objeto='" + result.getObjectName()
                + "'. " + "Núm. errores: " + result.getErrorCount());

        Map<String, String> errores = new HashMap<>();
        result.getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errores.put(fieldName, errorMessage);
        });

        problemDetail.setProperty("errores", errores);
        return problemDetail;
    }
}
