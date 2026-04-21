package es.pcomida.proyectocomida.plato.dto;

import es.pcomida.proyectocomida.plato.models.Categoria;
import es.pcomida.proyectocomida.plato.models.Tipo;
import es.pcomida.proyectocomida.plato.models.Variante;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlatoUpdateDto {
    
    private String nombre;
    private String descripcion;
    private Variante variante;
    private Categoria categoria;
    private Tipo tipo;

    @Positive(message = "El precio debe ser mayor que 0")
    private Double precio;

    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;
}
