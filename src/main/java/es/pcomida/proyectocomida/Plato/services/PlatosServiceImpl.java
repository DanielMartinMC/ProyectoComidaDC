package es.pcomida.proyectocomida.Plato.services;

import es.pcomida.proyectocomida.Plato.mapper.PlatoMapper;
import es.pcomida.proyectocomida.Plato.dto.PlatoCreateDto;
import es.pcomida.proyectocomida.Plato.dto.PlatoResponseDto;
import es.pcomida.proyectocomida.Plato.dto.PlatoUpdateDto;
import es.pcomida.proyectocomida.Plato.exceptions.PlatoNotFoundException;
import es.pcomida.proyectocomida.Plato.models.Plato;
import es.pcomida.proyectocomida.Plato.repositories.PlatosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = "platos")
public class PlatosServiceImpl implements PlatosService {
    private final PlatosRepository platosRepository;
    private final PlatoMapper platoMapper;

    @Override
    public List<PlatoResponseDto> findAll(String nombre, Enum tipo) {
        // Si todo está vacío o nulo, devolvemos todos los Platos
        if ((nombre == null || nombre.isEmpty()) && (tipo == null)) {
            log.info("Buscando todos los Platos");
            return platoMapper.toResponseDtoList(platosRepository.findAll());
        }
        // Si la nombre no está vacía, pero la categoría si, buscamos por nombre
        if ((nombre != null && !nombre.isEmpty()) && (tipo == null)) {
            log.info("Buscando productos por nombre: " + nombre);
            return platoMapper.toResponseDtoList(platosRepository.findByNombre(nombre));
        }
        // Si la nombre está vacía, pero la categoría no, buscamos por categoría
        if (nombre == null || nombre.isEmpty()) {
            log.info("Buscando productos por tipo: " + tipo);
            return platoMapper.toResponseDtoList(platosRepository.findByTipoContainsIgnoreCase(tipo));
        }
        // Si la nombre y la categoría no están vacías, buscamos por ambas
        log.info("Buscando productos por nombre: " + nombre + " y categoría: " + tipo);
        return platoMapper.toResponseDtoList(platosRepository.findByNombreAndTipoContainsIgnoreCase(nombre, tipo));
    }

    @Override
    @Cacheable(key = "#id")
    public PlatoResponseDto findById(Long id) {
        log.info("Buscando producto por id: " + id);
        return platoMapper.toPlatoResponseDto(platosRepository.findById(id)
                .orElseThrow(() -> new PlatoNotFoundException(id)));
    }


    @CachePut
    @Override
    public PlatoResponseDto save(PlatoCreateDto platoCreateDto) {
        log.info("Guardando producto: " + platoCreateDto);
        // obtenemos el id de producto
        // Creamos el producto nuevo con los datos que nos vienen del dto, podríamos usar el mapper
        Plato nuevoPlato = platoMapper.toPlato(platoCreateDto);
        // Lo guardamos en el repositorio
        return platoMapper.toPlatoResponseDto(platosRepository.save(nuevoPlato));
    }

    @CachePut
    @Override
    public PlatoResponseDto update(Long id, PlatoUpdateDto platoUpdateDto) {
        log.info("Actualizando producto por id: " + id);
        // Si no existe lanza excepción, por eso ya llamamos a lo que hemos implementado antes
        var PlatoActual = platosRepository.findById(id).orElseThrow(() -> new PlatoNotFoundException(id));
        // Actualizamos el producto con los datos que nos vienen del dto, podríamos usar el mapper
        Plato productoActualizado = platoMapper.toPlato(platoUpdateDto, PlatoActual);
        // Lo guardamos en el repositorio
        return platoMapper.toPlatoResponseDto(platosRepository.save(productoActualizado));
    }

    @Override
    @CacheEvict
    public void deleteById(Long id) {
        log.debug("Borrando producto por id: " + id);
        // Si no existe lanza excepción, por eso ya llamamos a lo que hemos implementado antes
        this.findById(id);
        // Lo borramos del repositorio
        platosRepository.deleteById(id);

    }
}
