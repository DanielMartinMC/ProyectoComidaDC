package es.pcomida.proyectocomida.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {
    private String nombre;
    private String apellidos;
    private String username;
    private String email;
    private String direccion;
    private String codigoPostal;
    private String telefono;
    private String ciudad;
    private String pais;
    private Set<String> roles;
}
