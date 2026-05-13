package es.pcomida.proyectocomida.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @NotBlank(message = "El username no puede estar vacío")
    private String username;

    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @NotBlank(message = "La password no puede estar vacía")
    @Length(min = 10, message = "La Password debe tener al menos 10 caracteres")
    private String password;

    @NotBlank(message = "La confirmación de la password no puede estar vacía")
    private String passwordConfirm;
    
    // Añadimos los campos que faltaban del UsuarioCreateDTO
    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;

    private String direccion;

    private String codigoPostal;

    private String ciudad;

    private String pais;
}
