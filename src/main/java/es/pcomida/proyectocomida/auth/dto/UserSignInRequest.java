package es.pcomida.proyectocomida.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInRequest {
    @NotBlank(message = "Username no puede estar vacío")
    private String username;

    @NotBlank(message = "Password no puede estar vacía")
    private String password;
}
