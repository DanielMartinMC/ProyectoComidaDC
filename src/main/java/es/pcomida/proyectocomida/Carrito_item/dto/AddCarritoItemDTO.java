package es.pcomida.proyectocomida.Carrito_item.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddCarritoItemDTO {
    @Positive
    private Long platoID;

    @Positive(message = "Tiene que haber una unidad como minimo")
    private Integer cantidad;
}
