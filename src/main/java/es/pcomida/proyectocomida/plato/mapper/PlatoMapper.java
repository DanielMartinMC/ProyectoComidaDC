package es.pcomida.proyectocomida.plato.mapper;

import es.pcomida.proyectocomida.plato.dto.PlatoCreateDto;
import es.pcomida.proyectocomida.plato.dto.PlatoResponseDto;
import es.pcomida.proyectocomida.plato.dto.PlatoUpdateDto;
import es.pcomida.proyectocomida.plato.models.Plato;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlatoMapper {

    public Plato toPlato(PlatoCreateDto platoCreateDto) {
        return Plato.builder()
                .nombre(platoCreateDto.getNombre())
                .descripcion(platoCreateDto.getDescripcion())
                .tipo(platoCreateDto.getTipo())
                .categoria(platoCreateDto.getCategoria())
                .variante(platoCreateDto.getVariante())
                .precio(platoCreateDto.getPrecio())
                .cantidad(platoCreateDto.getCantidad())
                .isDeleted(false) // Por defecto al crear
                .build();
    }

    public Plato toPlato(PlatoUpdateDto platoUpdateDto, Plato plato) {
        // No usamos el builder para no perder propiedades no incluidas en el DTO (como isDeleted)
        plato.setNombre(platoUpdateDto.getNombre() != null ? platoUpdateDto.getNombre() : plato.getNombre());
        plato.setDescripcion(platoUpdateDto.getDescripcion() != null ? platoUpdateDto.getDescripcion() : plato.getDescripcion());
        plato.setPrecio(platoUpdateDto.getPrecio() != null ? platoUpdateDto.getPrecio() : plato.getPrecio());
        plato.setCantidad(platoUpdateDto.getCantidad() != null ? platoUpdateDto.getCantidad() : plato.getCantidad());
        plato.setCategoria(platoUpdateDto.getCategoria() != null ? platoUpdateDto.getCategoria() : plato.getCategoria());
        plato.setVariante(platoUpdateDto.getVariante() != null ? platoUpdateDto.getVariante() : plato.getVariante());
        plato.setTipo(platoUpdateDto.getTipo() != null ? platoUpdateDto.getTipo() : plato.getTipo());
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
                .cantidad(plato.getCantidad())
                .build();
    }

    public List<PlatoResponseDto> toResponseDtoList(List<Plato> platos) {
        return platos.stream()
                .map(this::toPlatoResponseDto)
                .toList();
    }
}
