package es.pcomida.proyectocomida.plato.services;

import es.pcomida.proyectocomida.plato.dto.PlatoCreateDto;
import es.pcomida.proyectocomida.plato.dto.PlatoResponseDto;
import es.pcomida.proyectocomida.plato.dto.PlatoUpdateDto;
import es.pcomida.proyectocomida.plato.models.Tipo;
import es.pcomida.proyectocomida.usuario.models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PlatosService {

    Page<PlatoResponseDto> findAll(Optional<String> nombre, Optional<Tipo> tipo, Optional<Boolean> isDeleted, Pageable pageable, Usuario usuario);

    PlatoResponseDto findById(Long id);

    PlatoResponseDto save(PlatoCreateDto platoCreateDto);

    PlatoResponseDto update(Long id, PlatoUpdateDto platoUpdateDto);

    void deleteById(Long id);
}
