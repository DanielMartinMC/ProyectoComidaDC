package es.pcomida.proyectocomida.plato.services;

import es.pcomida.proyectocomida.plato.dto.PlatoCreateDto;
import es.pcomida.proyectocomida.plato.dto.PlatoResponseDto;
import es.pcomida.proyectocomida.plato.dto.PlatoUpdateDto;
import es.pcomida.proyectocomida.plato.exceptions.PlatoNotFoundException;
import es.pcomida.proyectocomida.plato.mapper.PlatoMapper;
import es.pcomida.proyectocomida.plato.models.Plato;
import es.pcomida.proyectocomida.plato.models.Tipo;
import es.pcomida.proyectocomida.plato.repositories.PlatosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = "platos")
public class PlatosServiceImpl implements PlatosService {
    private final PlatosRepository platosRepository;
    private final PlatoMapper platoMapper;

    @Override
    public Page<PlatoResponseDto> findAll(Optional<String> nombre, Optional<Tipo> tipo, Optional<Boolean> isDeleted, Pageable pageable) {
        log.info("Buscando platos con filtros: nombre={}, tipo={}, isDeleted={}", nombre, tipo, isDeleted);

        Specification<Plato> specNombre = (root, query, cb) ->
                nombre.map(n -> cb.like(cb.lower(root.get("nombre")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Plato> specTipo = (root, query, cb) ->
                tipo.map(t -> cb.equal(root.get("tipo"), t))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Plato> specIsDeleted = (root, query, cb) ->
                isDeleted.map(d -> cb.equal(root.get("isDeleted"), d))
                        .orElseGet(() -> cb.isTrue(cb.literal(true)));

        Specification<Plato> criterio = Specification.where(specNombre)
                .and(specTipo)
                .and(specIsDeleted);

        Page<Plato> platoPage = platosRepository.findAll(criterio, pageable);
        return platoPage.map(platoMapper::toPlatoResponseDto);
    }

    @Override
    @Cacheable(key = "#id")
    public PlatoResponseDto findById(Long id) {
        log.info("Buscando plato por id: {}", id);
        return platoMapper.toPlatoResponseDto(platosRepository.findById(id)
                .orElseThrow(() -> new PlatoNotFoundException(id)));
    }

    @Override
    @CachePut(key = "#result.id")
    public PlatoResponseDto save(PlatoCreateDto platoCreateDto) {
        log.info("Guardando nuevo plato: {}", platoCreateDto);
        Plato nuevoPlato = platoMapper.toPlato(platoCreateDto);
        return platoMapper.toPlatoResponseDto(platosRepository.save(nuevoPlato));
    }

    @Override
    @CachePut(key = "#id")
    public PlatoResponseDto update(Long id, PlatoUpdateDto platoUpdateDto) {
        log.info("Actualizando plato con id: {}", id);
        Plato platoActual = platosRepository.findById(id).orElseThrow(() -> new PlatoNotFoundException(id));
        Plato platoActualizado = platoMapper.toPlato(platoUpdateDto, platoActual);
        return platoMapper.toPlatoResponseDto(platosRepository.save(platoActualizado));
    }

    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        log.info("Eliminando plato con id: {}", id);
        if (!platosRepository.existsById(id)) {
            throw new PlatoNotFoundException(id);
        }
        platosRepository.deleteById(id);
    }
}
