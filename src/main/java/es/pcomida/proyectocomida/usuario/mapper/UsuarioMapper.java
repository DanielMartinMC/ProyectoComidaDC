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

    public Usuario toUsuario(UsuarioUpdateDTO updateDTO, Usuario usuario) {
        if (updateDTO.getNombre() != null) {
            usuario.setNombre(updateDTO.getNombre());
        }
        if (updateDTO.getApellidos() != null) {
            usuario.setApellidos(updateDTO.getApellidos());
        }
        if (updateDTO.getUsername() != null) {
            usuario.setUsername(updateDTO.getUsername());
        }
        if (updateDTO.getEmail() != null) {
            usuario.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getTelefono() != null) {
            usuario.setTelefono(updateDTO.getTelefono());
        }
        if (updateDTO.getPassword() != null) {
            usuario.setPassword(updateDTO.getPassword());
        }
        if (updateDTO.getDireccion() != null) {
            usuario.setDireccion(updateDTO.getDireccion());
        }
        if (updateDTO.getCodigoPostal() != null) {
            usuario.setCodigoPostal(updateDTO.getCodigoPostal());
        }
        if (updateDTO.getCiudad() != null) {
            usuario.setCiudad(updateDTO.getCiudad());
        }
        if (updateDTO.getPais() != null) {
            usuario.setPais(updateDTO.getPais());
        }
        return usuario;
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
                .isSuscriptor(usuario.getIsSuscriptor())
                .suscripcionExpira(usuario.getSuscripcionExpira())
                .build();
    }

    public List<UsuarioResponseDTO> toResponseDTOlist(List<Usuario> usuarios){
        return usuarios.stream().map(this::toUsuarioResponseDTO).toList();
    }
}