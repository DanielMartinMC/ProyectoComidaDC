package es.pcomida.proyectocomida.Usuario.mapper;

import es.pcomida.proyectocomida.Carrito.models.Carrito;
import es.pcomida.proyectocomida.Usuario.dto.UsuarioCreateDTO;
import es.pcomida.proyectocomida.Usuario.models.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UsuarioMapper {
    public Usuario toUsuario(UsuarioCreateDTO usuarioCreateDTO, Carrito carrito) {
        return Usuario.builder()
                .id(null)
                .nombre(usuarioCreateDTO.getNombre())
                .apellidos(usuarioCreateDTO.getApellidos())
                .username(usuarioCreateDTO.getUsername())
                .email(usuarioCreateDTO.getEmail())
                .password(usuarioCreateDTO.getPassword())
                .direccion(usuarioCreateDTO.getDireccion())
                .codigoPostal(usuarioCreateDTO.getCodigoPostal())
                .ciudad(usuarioCreateDTO.getCiudad())
                .pais(usuarioCreateDTO.getPais())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())


    }
}
