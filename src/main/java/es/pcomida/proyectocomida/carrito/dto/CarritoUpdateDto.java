package es.pcomida.proyectocomida.carrito.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarritoUpdateDto {
    // El único campo que un usuario debería poder actualizar directamente en el carrito
    // es el código del cupón. El resto de campos (estado, total) se actualizan
    // como consecuencia de otras acciones (añadir/quitar productos, pagar, etc.).
    private String codigoCupon;
}
