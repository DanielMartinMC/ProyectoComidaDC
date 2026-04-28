package es.pcomida.proyectocomida.metodopago.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidNumeroTarjetaException extends RuntimeException {
    public InvalidNumeroTarjetaException(String message) {
        super(message);
    }
}
