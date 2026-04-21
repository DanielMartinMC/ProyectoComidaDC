package es.pcomida.proyectocomida.transaccionpago.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransaccionNotFoundException extends RuntimeException {
    public TransaccionNotFoundException(Long id) {
        super("No se ha encontrado la transacción con id: " + id);
    }

    public TransaccionNotFoundException(String message) {
        super(message);
    }
}
