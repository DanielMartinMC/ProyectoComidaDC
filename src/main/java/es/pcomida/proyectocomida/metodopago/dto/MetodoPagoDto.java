package es.pcomida.proyectocomida.metodopago.dto;

import es.pcomida.proyectocomida.metodopago.models.TipoMetodoPago;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetodoPagoDto {
    private Long id;
    private TipoMetodoPago tipo;
    private String numeroTarjeta;
    private String fechaExpiracion;
    private Boolean isDefault;
}
