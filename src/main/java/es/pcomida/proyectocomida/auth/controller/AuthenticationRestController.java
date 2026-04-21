package es.pcomida.proyectocomida.auth.controller;

import es.pcomida.proyectocomida.auth.dto.JwtAuthResponse;
import es.pcomida.proyectocomida.auth.dto.UserSignInRequest;
import es.pcomida.proyectocomida.auth.dto.UserSignUpRequest;
import es.pcomida.proyectocomida.auth.services.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthResponse> signUp(@Valid @RequestBody UserSignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> signIn(@Valid @RequestBody UserSignInRequest request) {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }
}
