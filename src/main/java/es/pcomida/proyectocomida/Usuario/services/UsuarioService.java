package es.pcomida.proyectocomida.Usuario.services;

import es.pcomida.proyectocomida.Usuario.dto.UsuarioCreateDTO;
import es.pcomida.proyectocomida.Usuario.dto.UsuarioResponseDTO;
import es.pcomida.proyectocomida.Usuario.dto.UsuarioUpdateDTO;
import es.pcomida.proyectocomida.Usuario.models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UsuarioService{
    Page<UsuarioResponseDTO> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable);

    UsuarioResponseDTO findById(Long id);

    UsuarioResponseDTO save(UsuarioCreateDTO usuarioCreateDTO);


    UsuarioResponseDTO update(Long id,UsuarioUpdateDTO usuarioUpdateDTO);

    //Borrado Fisico
    void deleteById(Long id);

    //Borrado Lógico
    void softDeleteById(Long id);







}
