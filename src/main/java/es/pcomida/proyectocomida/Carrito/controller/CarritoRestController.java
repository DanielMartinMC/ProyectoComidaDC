package es.pcomida.proyectocomida.Carrito.controller;

import es.pcomida.proyectocomida.Carrito.dto.CarritoCreateDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoResponseDto;
import es.pcomida.proyectocomida.Carrito.dto.CarritoUpdateDto;
import es.pcomida.proyectocomida.Carrito.models.Estados;
import es.pcomida.proyectocomida.Carrito.services.CarritoService;
import es.pcomida.proyectocomida.Carrito_item.dto.AddCarritoItemDTO;
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
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController // Es un controlador Rest
@RequestMapping("api/${api.version}/Carrito")
public class CarritoRestController {
    private final CarritoService carritoService;

    @GetMapping()
    public ResponseEntity<List<CarritoResponseDto>> findAll(@RequestParam(required = false)UUID usuario,
                                                             @RequestParam(required = false) Estados estados) {
        log.info("Buscando Carritos por usuario={}, tipo={}", usuario, estados);
        return ResponseEntity.ok(carritoService.findAll(usuario, estados));

    }

    @GetMapping("/id")
    public ResponseEntity<CarritoResponseDto> findById(@RequestParam Long id) {
        log.info("Buscando Carrito por id={}", id);
        return ResponseEntity.ok(carritoService.findById(id));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<CarritoResponseDto> addPlato(@PathVariable Long id,
                                                       @RequestBody AddCarritoItemDTO itemDTO){
        log.info("Añadiendo plato: {}, al carrito: {}", itemDTO, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.addPlatoToCarrito(id, itemDTO));
    }


    @PostMapping()
    public ResponseEntity<CarritoResponseDto> create(@Valid @RequestBody CarritoCreateDto carritoCreateDto) {
        log.info("Creando Carrito : {}", carritoCreateDto);
        var saved = carritoService.save(carritoCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    @PutMapping("/id")
    public ResponseEntity<CarritoResponseDto> update(@PathVariable Long id, @Valid @RequestBody CarritoUpdateDto carritoUpdateDto) {
        log.info("Actualizando Carrito id={} con Carrito={}", id, carritoUpdateDto);
        return ResponseEntity.ok(carritoService.update(id, carritoUpdateDto));

    }

    @PatchMapping("/id")
    public ResponseEntity<CarritoResponseDto> updatePartial(@PathVariable Long id, @Valid @RequestBody CarritoUpdateDto carritoUpdateDto) {
        log.info("Actualizando parcialmente Carrito con id={} con Carrito={}",id, carritoUpdateDto);
        return ResponseEntity.ok(carritoService.update(id, carritoUpdateDto));
    }

    @DeleteMapping("/id")
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
