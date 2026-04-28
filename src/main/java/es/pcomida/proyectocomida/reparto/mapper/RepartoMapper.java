package es.pcomida.proyectocomida.reparto.mapper;

import es.pcomida.proyectocomida.pedido.models.Pedido;
import es.pcomida.proyectocomida.reparto.dto.RepartoCreateDto;
import es.pcomida.proyectocomida.reparto.dto.RepartoResponseDto;
import es.pcomida.proyectocomida.reparto.dto.RepartoUpdateDto;
import es.pcomida.proyectocomida.reparto.models.Reparto;
import es.pcomida.proyectocomida.usuario.models.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RepartoMapper {
    public Reparto toReparto(RepartoCreateDto repartoCreateDto, Usuario usuario, Pedido pedido) {
        return Reparto.builder()
                .id(null)
                .usuario(usuario)
                .pedido_id(pedido)
                .repartidorID(repartoCreateDto.getRepartidorID())
                .direccionDestino(repartoCreateDto.getDireccionDestino() != null ? repartoCreateDto.getDireccionDestino() : usuario.getDireccion())
                .ciudad(repartoCreateDto.getCiudad() != null ? repartoCreateDto.getCiudad() : usuario.getCiudad())
                .telefonoContacto(repartoCreateDto.getTelefonoContacto() != null ? repartoCreateDto.getTelefonoContacto() : usuario.getTelefono())
                .estado(repartoCreateDto.getEstado())
                .build();
    }

    public Reparto toReparto(RepartoUpdateDto dto, Reparto reparto) {
        if (dto.getRepartidorID() != null) {
            reparto.setRepartidorID(dto.getRepartidorID());
        }
        if (dto.getDireccionDestino() != null) {
            reparto.setDireccionDestino(dto.getDireccionDestino());
        }
        if (dto.getCiudad() != null) {
            reparto.setCiudad(dto.getCiudad());
        }
        if (dto.getTelefonoContacto() != null) {
            reparto.setTelefonoContacto(dto.getTelefonoContacto());
        }
        if (dto.getEstado() != null) {
            reparto.setEstado(dto.getEstado());
        }
        return reparto;
    }

    public RepartoResponseDto toRepartoResponseDto(Reparto reparto) {
        return RepartoResponseDto.builder()
                .id(reparto.getId())
                .usuarioId(reparto.getUsuario().getId())
                .repartidorID(reparto.getRepartidorID())
                .pedidoId(reparto.getPedido_id().getId())
                .direccionDestino(reparto.getDireccionDestino())
                .ciudad(reparto.getCiudad())
                .telefonoContacto(reparto.getTelefonoContacto())
                .estado(reparto.getEstado())
                .build();
    }

    List<RepartoResponseDto> toRepartoResponseDtoList(List<Reparto> reparto) {
        return reparto.stream().map(this::toRepartoResponseDto).toList();
    }
}
