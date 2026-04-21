package es.pcomida.proyectocomida.reparto.dto;

import es.pcomida.proyectocomida.reparto.models.RepartoEstado;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RepartoResponseDto {
    private Long id;

    private Long usuarioId;

    private Long repartidorID;

    private Long pedidoId;

    private String direccionDestino;

    private String ciudad;

    private String telefonoContacto;

    private RepartoEstado estado;
}
