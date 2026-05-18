package es.pcomida.proyectocomida.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {
    private String nombre;
    private String apellidos;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String direccion;
    private String codigoPostal;
    private String telefono;
    private String ciudad;
    private String pais;
}
