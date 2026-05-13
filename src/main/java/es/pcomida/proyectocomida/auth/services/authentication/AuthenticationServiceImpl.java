package es.pcomida.proyectocomida.auth.services.authentication;

import es.pcomida.proyectocomida.auth.dto.JwtAuthResponse;
import es.pcomida.proyectocomida.auth.dto.UserSignInRequest;
import es.pcomida.proyectocomida.auth.dto.UserSignUpRequest;
import es.pcomida.proyectocomida.auth.exceptions.AuthDifferentPasswords;
import es.pcomida.proyectocomida.auth.exceptions.AuthExistingUsernameOrEmail;
import es.pcomida.proyectocomida.auth.repositories.AuthUserRepository;
import es.pcomida.proyectocomida.auth.services.jwt.JwtService;
import es.pcomida.proyectocomida.usuario.models.Roles;
import es.pcomida.proyectocomida.usuario.models.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthResponse signUp(UserSignUpRequest request) {
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new AuthDifferentPasswords("Las contraseñas no coinciden");
        }
        if (authUserRepository.existsByUsername(request.getUsername())) {
            throw new AuthExistingUsernameOrEmail("El nombre de usuario ya está en uso");
        }
        if (authUserRepository.existsByEmail(request.getEmail())) {
            throw new AuthExistingUsernameOrEmail("El email ya está en uso");
        }

        var user = Usuario.builder()
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .telefono(request.getTelefono())
                .direccion(request.getDireccion() != null ? request.getDireccion() : "")
                .codigoPostal(request.getCodigoPostal() != null ? request.getCodigoPostal() : "")
                .ciudad(request.getCiudad() != null ? request.getCiudad() : "")
                .pais(request.getPais() != null ? request.getPais() : "")
                .roles(Set.of(Roles.USER)) // Por defecto, rol USER
                .isDeleted(false)
                .build();

        var savedUser = authUserRepository.save(user);
        var jwtToken = jwtService.generateToken(savedUser);
        return JwtAuthResponse.builder().token(jwtToken).build();
    }

    @Override
    public JwtAuthResponse signIn(UserSignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        var user = authUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        var jwtToken = jwtService.generateToken(user);
        return JwtAuthResponse.builder().token(jwtToken).build();
    }
}
