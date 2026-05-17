package es.pcomida.proyectocomida.plato.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlatoBadRequestException extends PlatoException {
    public PlatoBadRequestException(String mensaje) {
        super(mensaje);
    }
}
