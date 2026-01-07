package es.pcomida.proyectocomida.Carrito.excepctions;

public class CarritoBadRequestException extends RuntimeException {
    public CarritoBadRequestException(String message) {
        super(message);
    }
}
