package es.pcomida.proyectocomida.Plato.mapper;

import es.pcomida.proyectocomida.Plato.dto.PlatoCreateDto;
import es.pcomida.proyectocomida.Plato.dto.PlatoResponseDto;
import es.pcomida.proyectocomida.Plato.dto.PlatoUpdateDto;
import es.pcomida.proyectocomida.Plato.models.Plato;
/*import es.pcomida.proyectocomida.Usuario.models.Usuario;*/
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlatoMapper {
    public Plato toPlato(PlatoCreateDto platoCreateDto/*, Usuario usuario*/) {
        return Plato.builder()
                .id(null)
                .nombre(platoCreateDto.getNombre())
                .descripcion(platoCreateDto.getDescripcion())
                .tipo(platoCreateDto.getTipo())
                .categoria(platoCreateDto.getCategoria())
                .variante(platoCreateDto.getVariante())
                /*.usuario(usuario)*/
                .precio(platoCreateDto.getPrecio())
                .cantidad(platoCreateDto.getCantidad())
                .build();
    }

    public Plato toPlato(PlatoUpdateDto platoUpdateDto, Plato plato) {
        return Plato.builder()
                .id(plato.getId())
                .nombre(platoUpdateDto.getNombre() != null ? platoUpdateDto.getNombre() : plato.getNombre())
                .descripcion(platoUpdateDto.getDescripcion() != null ? platoUpdateDto.getDescripcion() : plato.getDescripcion())
                .precio(platoUpdateDto.getPrecio() != null ? platoUpdateDto.getPrecio() : plato.getPrecio())
                .cantidad(platoUpdateDto.getCantidad() != null ? platoUpdateDto.getCantidad() : plato.getCantidad())
                .categoria(platoUpdateDto.getCategoria() != null ? platoUpdateDto.getCategoria() : plato.getCategoria())
                .variante(platoUpdateDto.getVariante() != null ? platoUpdateDto.getVariante() : plato.getVariante())
                /*.usuario(plato.getUsuario())*/
                .build();
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
                /*.usuario(plato.getUsuario().getNombre())*/
                .cantidad(plato.getCantidad())
                .build();
    }

    // Mapeamos de modelo a DTO (lista)
    public List<PlatoResponseDto> toResponseDtoList(List<Plato> platos) {
        return platos.stream()
                .map(this::toPlatoResponseDto)
                .toList();
    }

}