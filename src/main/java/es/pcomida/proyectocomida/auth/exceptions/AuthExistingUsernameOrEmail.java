package es.pcomida.proyectocomida.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict es un buen código para "ya existe"
public class AuthExistingUsernameOrEmail extends AuthException {
    public AuthExistingUsernameOrEmail(String message) {
        super(message);
    }
}
