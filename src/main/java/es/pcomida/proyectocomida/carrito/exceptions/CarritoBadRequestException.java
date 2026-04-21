package es.pcomida.proyectocomida.carrito.exceptions;

public class CarritoBadRequestException extends RuntimeException {
    public CarritoBadRequestException(String message) {
        super(message);
    }
}
