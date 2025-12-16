package es.pcomida.proyectocomida.Plato.exceptions;

public abstract class PlatoException extends RuntimeException {
    public PlatoException(String message) {
        super(message);
    }
}
