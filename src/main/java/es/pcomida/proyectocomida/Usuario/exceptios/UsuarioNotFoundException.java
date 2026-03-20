package es.pcomida.proyectocomida.Usuario.exceptios;

public class UsuarioNotFoundException extends UsuarioException {
    public UsuarioNotFoundException(Long id) {
        super("No se ha encontrado al usuario por el id: " + id);
    }

    public UsuarioNotFoundException (String username){
        super("No se ha encontrado al usuario por el username: " + username);
    }


}
