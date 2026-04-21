package es.pcomida.proyectocomida.reparto.services;

import es.pcomida.proyectocomida.pedido.exceptions.PedidoNotFoundException;
import es.pcomida.proyectocomida.pedido.models.Pedido;
import es.pcomida.proyectocomida.pedido.repositories.PedidosRepository;
import es.pcomida.proyectocomida.reparto.dto.RepartoCreateDto;
import es.pcomida.proyectocomida.reparto.dto.RepartoResponseDto;
import es.pcomida.proyectocomida.reparto.dto.RepartoUpdateDto;
import es.pcomida.proyectocomida.reparto.exceptions.RepartoNotFoundException;
import es.pcomida.proyectocomida.reparto.mapper.RepartoMapper;
import es.pcomida.proyectocomida.reparto.models.Reparto;
import es.pcomida.proyectocomida.reparto.models.RepartoEstado;
import es.pcomida.proyectocomida.reparto.repositories.RepartoRepository;
import es.pcomida.proyectocomida.usuario.exceptions.UsuarioNotFoundException;
import es.pcomida.proyectocomida.usuario.models.Usuario;
import es.pcomida.proyectocomida.usuario.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@CacheConfig(cacheNames = {"repartos"})
@Slf4j
@RequiredArgsConstructor
@Service
public class RepartoServiceImpl implements RepartoService, InitializingBean {
    private final RepartoRepository repartoRepository;
    private final RepartoMapper repartoMapper;
    private final PedidosRepository pedidosRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public Page<RepartoResponseDto> findAll(Optional<RepartoEstado> estado, Optional<String> ciudad,Optional<Long>repartidor_id ,Pageable pageable) {
        log.info("Buscando repartos con los filtros: esatdo={}, ciudad={}",estado,ciudad);

        Specification<Reparto> specRepartoStatus = ((root, query, criteriaBuilder) ->
                estado.map(e -> criteriaBuilder.equal(root.get("estado"), e))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))));
        Specification<Reparto> specRepartoCiudad = ((root, query, criteriaBuilder) ->
                ciudad.map(c -> criteriaBuilder.like(root.get("ciudad"), c))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))));
        Specification<Reparto> spepecRepartoRepartidor =(((root, query, criteriaBuilder) ->
                repartidor_id.map(r -> criteriaBuilder.equal(root.get("repartidor_id"), r))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)))));

        Specification<Reparto> criterio = Specification.where(specRepartoStatus)
                .and(specRepartoCiudad).and(spepecRepartoRepartidor);

        Page<Reparto> repartoPage = repartoRepository.findAll(criterio, pageable);
        return repartoPage.map(repartoMapper::toRepartoResponseDto);
    }

    @Override
    @Cacheable(key = "#id")
    public RepartoResponseDto findById(Long id) {
        log.info("Buscando reparto con id: {}", id);
        return repartoRepository.findById(id)
                .map(repartoMapper::toRepartoResponseDto)
                .orElseThrow(() -> new RepartoNotFoundException(id));
    }

    @Override
    @CachePut(key = "#result.id")
    public RepartoResponseDto save(RepartoCreateDto repartoCreateDto) {
        log.info("Guardando nuevo reparto");
        Usuario usuario = usuarioRepository.findById(repartoCreateDto.getUsuario().getId())
                .orElseThrow(() -> new UsuarioNotFoundException(repartoCreateDto.getUsuario().getId()));
        
        Pedido pedido = pedidosRepository.findById(repartoCreateDto.getPedido_id().getId())
                .orElseThrow(() -> new PedidoNotFoundException(repartoCreateDto.getPedido_id().getId()));
        
        Reparto reparto = repartoMapper.toReparto(repartoCreateDto, usuario, pedido);
        Reparto savedReparto = repartoRepository.save(reparto);
        return repartoMapper.toRepartoResponseDto(savedReparto);
    }

    @Override
    @CachePut(key = "#result.id")
    public RepartoResponseDto update(Long id, RepartoUpdateDto repartoUpdateDto) {
        log.info("Actualizando reparto con id: {}", id);
        Reparto reparto = repartoRepository.findById(id)
                .orElseThrow(() -> new RepartoNotFoundException(id));
        
        if (repartoUpdateDto.getRepartidorID() != null) {
            reparto.setRepartidorID(repartoUpdateDto.getRepartidorID());
        }
        if (repartoUpdateDto.getDireccionDestino() != null) {
            reparto.setDireccionDestino(repartoUpdateDto.getDireccionDestino());
        }
        if (repartoUpdateDto.getCiudad() != null) {
            reparto.setCiudad(repartoUpdateDto.getCiudad());
        }
        if (repartoUpdateDto.getTelefonoContacto() != null) {
            reparto.setTelefonoContacto(repartoUpdateDto.getTelefonoContacto());
        }
        if (repartoUpdateDto.getEstado() != null) {
            reparto.setEstado(repartoUpdateDto.getEstado());
        }
        
        Reparto updatedReparto = repartoRepository.save(reparto);
        return repartoMapper.toRepartoResponseDto(updatedReparto);
    }

    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        log.info("Borrando reparto con id: {}", id);
        if (!repartoRepository.existsById(id)) {
            throw new RepartoNotFoundException(id);
        }
        repartoRepository.deleteById(id);
    }

    @Override
    @CachePut(key = "#repartoId")
    public RepartoResponseDto asignarRepartidor(Long repartoId, Long repartidorId) {
        log.info("Asignando repartidor {} al reparto {}", repartidorId, repartoId);
        Reparto reparto = repartoRepository.findById(repartoId)
                .orElseThrow(() -> new RepartoNotFoundException(repartoId));
        
        reparto.setRepartidorID(repartidorId);
        if (reparto.getEstado() == RepartoEstado.PENDIENTE) {
            reparto.setEstado(RepartoEstado.EN_PREPARACION);
        }
        
        Reparto updatedReparto = repartoRepository.save(reparto);
        return repartoMapper.toRepartoResponseDto(updatedReparto);
    }

    @Override
    @CachePut(key = "#repartoId")
    public RepartoResponseDto actualizarEstado(Long repartoId, RepartoEstado nuevoEstado) {
        log.info("Actualizando estado del reparto {} a {}", repartoId, nuevoEstado);
        Reparto reparto = repartoRepository.findById(repartoId)
                .orElseThrow(() -> new RepartoNotFoundException(repartoId));
        
        reparto.setEstado(nuevoEstado);
        
        Reparto updatedReparto = repartoRepository.save(reparto);
        return repartoMapper.toRepartoResponseDto(updatedReparto);
    }

    @Override
    @CachePut(key = "#repartoId")
    public RepartoResponseDto cancelarReparto(Long repartoId) {
        log.info("Cancelando reparto {}", repartoId);
        Reparto reparto = repartoRepository.findById(repartoId)
                .orElseThrow(() -> new RepartoNotFoundException(repartoId));
        
        reparto.setEstado(RepartoEstado.CANCELADO);
        
        Reparto updatedReparto = repartoRepository.save(reparto);
        return repartoMapper.toRepartoResponseDto(updatedReparto);
    }

    @Override
    public Page<RepartoResponseDto> findRepartosByUsuario(Long usuarioId, Pageable pageable) {
        log.info("Buscando repartos del usuario {}", usuarioId);
        Page<Reparto> repartos = repartoRepository.findRepartoByUsuarioId(usuarioId, pageable);
        return repartos.map(repartoMapper::toRepartoResponseDto);
    }

    @Override
    public Page<RepartoResponseDto> findRepartosByRepartidor(Long repartidorId, Pageable pageable) {
        log.info("Buscando repartos del repartidor {}", repartidorId);
        Page<Reparto> repartos = repartoRepository.findRepartoByRepartidorID(repartidorId, pageable);
        return repartos.map(repartoMapper::toRepartoResponseDto);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("RepartoServiceImpl initialized.");
    }
}
