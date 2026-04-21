package es.pcomida.proyectocomida.auth.services.authentication;

import es.pcomida.proyectocomida.auth.dto.JwtAuthResponse;
import es.pcomida.proyectocomida.auth.dto.UserSignInRequest;
import es.pcomida.proyectocomida.auth.dto.UserSignUpRequest;

public interface AuthenticationService {
    JwtAuthResponse signUp(UserSignUpRequest request);
    JwtAuthResponse signIn(UserSignInRequest request);
}
