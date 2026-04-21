package es.pcomida.proyectocomida.usuario.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(Long id) {
        super("No se ha encontrado el usuario con id: " + id);
    }
    
    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
