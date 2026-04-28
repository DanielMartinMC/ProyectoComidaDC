package es.pcomida.proyectocomida.metodopago.dto;

import es.pcomida.proyectocomida.metodopago.models.TipoMetodoPago;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetodoPagoUpdateDto {
    private TipoMetodoPago tipo;

    @Pattern(regexp = "^[0-9]{13,19}$", message = "El número de tarjeta debe contener entre 13 y 19 dígitos.")
    private String numeroTarjeta;

    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/([0-9]{2})$", message = "La fecha de expiración debe tener el formato MM/AA.")
    private String fechaExpiracion;

    private Boolean isDefault;
}
