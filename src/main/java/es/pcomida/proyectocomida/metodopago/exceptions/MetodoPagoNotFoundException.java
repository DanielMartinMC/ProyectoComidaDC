package es.pcomida.proyectocomida.metodopago.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MetodoPagoNotFoundException extends RuntimeException {
    public MetodoPagoNotFoundException(Long id) {
        super("No se pudo encontrar el método de pago con el ID: " + id);
    }
}
