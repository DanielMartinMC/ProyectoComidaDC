package es.pcomida.proyectocomida.reparto.services;

import es.pcomida.proyectocomida.reparto.dto.RepartoCreateDto;
import es.pcomida.proyectocomida.reparto.dto.RepartoResponseDto;
import es.pcomida.proyectocomida.reparto.dto.RepartoUpdateDto;
import es.pcomida.proyectocomida.reparto.models.RepartoEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RepartoService {

    Page<RepartoResponseDto> findAll(Optional<RepartoEstado> estado, Optional<String> ciudad, Optional<Long> repartidorId,Pageable pageable);
    RepartoResponseDto findById(Long id);
    RepartoResponseDto save(RepartoCreateDto repartoCreateDto);
    RepartoResponseDto update(Long id, RepartoUpdateDto repartoUpdateDto);
    void deleteById(Long id);

    // Lógica de negocio
    RepartoResponseDto asignarRepartidor(Long repartoId, Long repartidorId);
    RepartoResponseDto actualizarEstado(Long repartoId, RepartoEstado nuevoEstado);
    RepartoResponseDto cancelarReparto(Long repartoId);

    // Consultas para diferentes roles
    Page<RepartoResponseDto> findRepartosByUsuario(Long usuarioId, Pageable pageable);
    Page<RepartoResponseDto> findRepartosByRepartidor(Long repartidorId, Pageable pageable);

}
