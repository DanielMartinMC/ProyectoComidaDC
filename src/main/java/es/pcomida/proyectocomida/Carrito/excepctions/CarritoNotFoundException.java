package es.pcomida.proyectocomida.Carrito.excepctions;

public class CarritoNotFoundException extends RuntimeException {

    public CarritoNotFoundException(Long id) {
        super("Carrito con id " + id + " no encontrado");
    }
}
