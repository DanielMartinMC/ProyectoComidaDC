package es.pcomida.proyectocomida.Reparto.exceptions;

public class RepartoNotFoundException extends RuntimeException {
    public RepartoNotFoundException(String message) {
        super(message);
    }
}
