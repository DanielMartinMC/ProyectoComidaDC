package es.pcomida.proyectocomida.metodopago.dto;

import es.pcomida.proyectocomida.metodopago.models.TipoMetodoPago;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPagoCreateDto {

    @NotNull(message = "El tipo de método de pago no puede ser nulo")
    private TipoMetodoPago tipo;

    @NotBlank(message = "El número de tarjeta no puede estar vacío")
    @Pattern(
            regexp = "^[0-9]{13,19}$",
            message = "El número de tarjeta debe contener entre 13 y 19 dígitos."
    )
    private String numeroTarjeta;

    @NotBlank(message = "La fecha de expiración no puede estar vacía")
    @Pattern(
            regexp = "^(0[1-9]|1[0-2])\\/([0-9]{2})$",
            message = "La fecha de expiración debe tener el formato MM/AA."
    )
    private String fechaExpiracion;

    @Builder.Default
    private Boolean isDefault = false;
}