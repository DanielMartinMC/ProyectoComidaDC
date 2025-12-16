package es.pcomida.proyectocomida.Plato.services;

import es.pcomida.proyectocomida.Plato.dto.PlatoCreateDto;
import es.pcomida.proyectocomida.Plato.dto.PlatoResponseDto;
import es.pcomida.proyectocomida.Plato.dto.PlatoUpdateDto;
import es.pcomida.proyectocomida.Plato.models.Plato;
import es.pcomida.proyectocomida.Plato.models.Tipo;

import java.util.List;

public interface PlatosService {

    List<PlatoResponseDto> findAll(String nombre, Tipo tipo);

    /*List<PlatoResponseDto> findByEsVegetariano(String esVegetariano);*/

    PlatoResponseDto findById(Long id);

    PlatoResponseDto save(PlatoCreateDto PlatoCreateDto);

    PlatoResponseDto update(Long id, PlatoUpdateDto platoUpdateDto);

    void deleteById(Long id);
}
