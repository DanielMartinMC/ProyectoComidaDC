package es.pcomida.proyectocomida.reparto.exceptions;

public class RepartoNotFoundException extends RuntimeException {
    public RepartoNotFoundException(Long id) {
        super("No se ha encontrado el reparto con id: " + id);
    }
}
