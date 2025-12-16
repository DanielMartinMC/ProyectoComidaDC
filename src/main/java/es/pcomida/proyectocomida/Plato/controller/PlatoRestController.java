package es.pcomida.proyectocomida.Plato.controller;


import es.pcomida.proyectocomida.Plato.dto.PlatoCreateDto;
import es.pcomida.proyectocomida.Plato.dto.PlatoResponseDto;
import es.pcomida.proyectocomida.Plato.dto.PlatoUpdateDto;
import es.pcomida.proyectocomida.Plato.exceptions.PlatoBadRequestException;
import es.pcomida.proyectocomida.Plato.exceptions.PlatoNotFoundException;
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

/**
 * Controlador de productos del tipo RestController
 * Fijamos la ruta de acceso a este controlador
 * Usamos el repositorio de productos y lo inyectamos en el constructor con Autowired
 *
 * @RequiredArgsConstructor es una anotación Lombok que nos permite inyectar dependencias basadas
 * en las anotaciones @Controller, @Service, @Component, etc.
 * y que se encuentren en nuestro contenedor de Spring
 * con solo declarar las dependencias como final ya que el constructor lo genera Lombok
 */
@Slf4j
@RequiredArgsConstructor
@RestController // Es un controlador Rest
@RequestMapping("api/${api.version}/Plato") // Es la ruta del controlador
public class PlatoRestController {

    private final PlatosService platosService;


    @GetMapping()
    public ResponseEntity<List<PlatoResponseDto>> getAll(@RequestParam(required = false) String nombre,
                                                           @RequestParam(required = false) Tipo tipo) {
        log.info("Buscando platos por numero={}, titular={}", nombre, tipo);
        return ResponseEntity.ok(platosService.findAll(nombre, tipo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatoResponseDto> getById(@PathVariable Long id) {
        log.info("Buscando tarjeta por id={}", id);
        return ResponseEntity.ok(platosService.findById(id));
    }


    @PostMapping()
    public ResponseEntity<PlatoResponseDto> create(@Valid @RequestBody PlatoCreateDto platoCreateDto) {
        log.info("Creando plato : {}", platoCreateDto);
        var saved = platosService.save(platoCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatoResponseDto> update(@PathVariable Long id, @Valid @RequestBody PlatoUpdateDto platoUpdateDto) {
        log.info("Actualizando tarjeta id={} con tarjeta={}", id, platoUpdateDto);
        return ResponseEntity.ok(platosService.update(id, platoUpdateDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PlatoResponseDto> updatePartial(@PathVariable Long id, @Valid @RequestBody PlatoUpdateDto platoUpdateDto) {
        log.info("Actualizando parcialmente tarjeta con id={} con plato={}",id, platoUpdateDto);
        return ResponseEntity.ok(platosService.update(id, platoUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Borrando producto por id: {}", id);
        platosService.deleteById(id);
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
