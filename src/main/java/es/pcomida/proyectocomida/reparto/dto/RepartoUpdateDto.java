package es.pcomida.proyectocomida.reparto.dto;

import es.pcomida.proyectocomida.pedido.models.Pedido;
import es.pcomida.proyectocomida.reparto.models.RepartoEstado;
import es.pcomida.proyectocomida.usuario.models.Usuario;
import lombok.Data;

@Data
public class RepartoUpdateDto {
    private Long id;

    private Usuario usuario;

    private Long repartidorID;

    private Pedido pedido_id;

    private String direccionDestino;

    private String ciudad;

    private String telefonoContacto;

    private RepartoEstado estado = RepartoEstado.PENDIENTE;
}
