package es.pcomida.proyectocomida.carrito.exceptions;

public class CarritoNotFoundException extends RuntimeException {

    public CarritoNotFoundException(Long id) {
        super("Carrito con id " + id + " no encontrado");
    }
}
