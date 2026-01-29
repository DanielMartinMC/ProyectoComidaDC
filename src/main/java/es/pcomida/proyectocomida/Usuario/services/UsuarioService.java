package es.pcomida.proyectocomida.Usuario.services;

import es.pcomida.proyectocomida.Usuario.dto.UsuarioCreateDTO;
import es.pcomida.proyectocomida.Usuario.dto.UsuarioResponseDTO;
import es.pcomida.proyectocomida.Usuario.dto.UsuarioUpdateDTO;
import es.pcomida.proyectocomida.Usuario.models.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UsuarioService{
    List<UsuarioResponseDTO> findAll(String nombre, Optional<String> apellidos);

    UsuarioResponseDTO findById(Long id);

    UsuarioResponseDTO findByUserName(String name);

    UsuarioResponseDTO save(UsuarioCreateDTO usuarioCreateDTO);


    UsuarioResponseDTO update(UsuarioUpdateDTO usuarioUpdateDTO);

    //Borrado Fisico
    void deleteById(Long id);

    //Borrado Lógico
    void softDeleteById(Long id);







}
