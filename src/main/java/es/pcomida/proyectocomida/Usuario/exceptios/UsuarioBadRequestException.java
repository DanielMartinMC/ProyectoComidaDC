package es.pcomida.proyectocomida.Usuario.exceptios;

public class UsuarioBadRequestException extends RuntimeException {
    public UsuarioBadRequestException(String message) {
        super(message);
    }
}
