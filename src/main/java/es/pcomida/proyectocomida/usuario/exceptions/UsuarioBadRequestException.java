package es.pcomida.proyectocomida.usuario.exceptions;

public class UsuarioBadRequestException extends RuntimeException {
    public UsuarioBadRequestException(String message) {
        super(message);
    }
}
