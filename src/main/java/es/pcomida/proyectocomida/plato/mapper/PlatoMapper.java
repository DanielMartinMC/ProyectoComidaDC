package es.pcomida.proyectocomida.plato.mapper;

import es.pcomida.proyectocomida.plato.dto.PlatoCreateDto;
import es.pcomida.proyectocomida.plato.dto.PlatoResponseDto;
import es.pcomida.proyectocomida.plato.dto.PlatoUpdateDto;
import es.pcomida.proyectocomida.plato.models.Plato;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlatoMapper {

    public Plato toPlato(PlatoCreateDto dto) {
        return Plato.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .tipo(dto.getTipo())
                .categoria(dto.getCategoria())
                .pais(dto.getPais())
                .variante(dto.getVariante())
                .precio(dto.getPrecio())
                .cantidad(dto.getCantidad())
                .premium(dto.isPremium())
                .imageUrl(dto.getImageUrl())
                .isDeleted(false)
                .build();
    }

    public Plato toPlato(PlatoUpdateDto dto, Plato plato) {
        plato.setNombre(dto.getNombre() != null ? dto.getNombre() : plato.getNombre());
        plato.setDescripcion(dto.getDescripcion() != null ? dto.getDescripcion() : plato.getDescripcion());
        plato.setPrecio(dto.getPrecio() != null ? dto.getPrecio() : plato.getPrecio());
        plato.setCantidad(dto.getCantidad() != null ? dto.getCantidad() : plato.getCantidad());
        plato.setCategoria(dto.getCategoria() != null ? dto.getCategoria() : plato.getCategoria());
        plato.setVariante(dto.getVariante() != null ? dto.getVariante() : plato.getVariante());
        plato.setTipo(dto.getTipo() != null ? dto.getTipo() : plato.getTipo());
        plato.setPais(dto.getPais() != null ? dto.getPais() : plato.getPais());
        if (dto.getIsPremium() != null) plato.setPremium(dto.getIsPremium());
        if (dto.getImageUrl() != null) plato.setImageUrl(dto.getImageUrl());
        return plato;
    }

    public PlatoResponseDto toPlatoResponseDto(Plato plato) {
        return PlatoResponseDto.builder()
                .id(plato.getId())
                .nombre(plato.getNombre())
                .descripcion(plato.getDescripcion())
                .precio(plato.getPrecio())
                .tipo(plato.getTipo())
                .variante(plato.getVariante())
                .categoria(plato.getCategoria())
                .pais(plato.getPais())
                .cantidad(plato.getCantidad())
                .isPremium(plato.isPremium())
                .imageUrl(plato.getImageUrl())
                .build();
    }

    public List<PlatoResponseDto> toResponseDtoList(List<Plato> platos) {
        return platos.stream()
                .map(this::toPlatoResponseDto)
                .toList();
    }
}