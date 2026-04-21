package es.pcomida.proyectocomida.usuario.mapper;

import es.pcomida.proyectocomida.carrito.models.Carrito;
import es.pcomida.proyectocomida.usuario.dto.UsuarioCreateDTO;
import es.pcomida.proyectocomida.usuario.dto.UsuarioResponseDTO;
import es.pcomida.proyectocomida.usuario.dto.UsuarioUpdateDTO;
import es.pcomida.proyectocomida.usuario.models.Roles;
import es.pcomida.proyectocomida.usuario.models.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {
    public Usuario toUsuario(UsuarioCreateDTO usuarioCreateDTO, Carrito carrito) {
        return Usuario.builder()
                .id(null)
                .nombre(usuarioCreateDTO.getNombre())
                .apellidos(usuarioCreateDTO.getApellidos())
                .username(usuarioCreateDTO.getUsername())
                .email(usuarioCreateDTO.getEmail())
                .telefono(usuarioCreateDTO.getTelefono())
                .password(usuarioCreateDTO.getPassword())
                .direccion(usuarioCreateDTO.getDireccion())
                .codigoPostal(usuarioCreateDTO.getCodigoPostal())
                .ciudad(usuarioCreateDTO.getCiudad())
                .pais(usuarioCreateDTO.getPais())
                .roles(Set.of(Roles.USER))
                .isSuscriptor(false)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();


    }
    public Usuario toUsuario(UsuarioUpdateDTO updateDTO,Usuario usuario){
        return Usuario.builder()
                .id(usuario.getId())
                .nombre(updateDTO.getNombre() != null ? updateDTO.getNombre() : usuario.getNombre())
                .apellidos(updateDTO.getApellidos() != null ? updateDTO.getApellidos(): usuario.getApellidos())
                .username(updateDTO.getUsername() != null ? updateDTO.getUsername() : usuario.getUsername())
                .email(updateDTO.getEmail() != null ? updateDTO.getEmail() : usuario.getEmail())
                .telefono(updateDTO.getTelefono() != null ? updateDTO.getTelefono() : usuario.getTelefono())
                .password(updateDTO.getPassword() != null ? updateDTO.getPassword() : usuario.getPassword())
                .direccion(updateDTO.getDireccion()  != null ? updateDTO.getDireccion() : usuario.getDireccion())
                .codigoPostal(updateDTO.getCodigoPostal()  != null ? updateDTO.getCodigoPostal() : usuario.getCodigoPostal())
                .ciudad(updateDTO.getCiudad() != null ? updateDTO.getCiudad() : usuario.getCiudad())
                .pais(updateDTO.getPais() != null ? updateDTO.getPais() : usuario.getPais())
                .build();
    }

    public UsuarioResponseDTO toUsuarioResponseDTO(Usuario usuario){
        return UsuarioResponseDTO.builder()
                .nombre(usuario.getNombre())
                .apellidos(usuario.getApellidos())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .direccion(usuario.getDireccion())
                .codigoPostal(usuario.getCodigoPostal())
                .ciudad(usuario.getCiudad())
                .pais(usuario.getPais())
                .roles(usuario.getRoles().stream().map(Enum::name).collect(Collectors.toSet()))
                .build();
    }

    public List<UsuarioResponseDTO> toResponseDTOlist(List<Usuario> usuarios){
        return usuarios.stream().map(this::toUsuarioResponseDTO).toList();

    }
}
