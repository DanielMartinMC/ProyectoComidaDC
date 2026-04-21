package es.pcomida.proyectocomida.plato.exceptions;

public abstract class PlatoException extends RuntimeException {
    public PlatoException(String message) {
        super(message);
    }
}
