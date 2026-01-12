package es.pcomida.proyectocomida.Plato.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlatoNotFoundException  extends PlatoException{
    public PlatoNotFoundException(Long id) {
        super("Plato con id " + id + " no encontrado");
    }

}