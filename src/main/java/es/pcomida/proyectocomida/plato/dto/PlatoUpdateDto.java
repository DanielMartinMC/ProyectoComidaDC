package es.pcomida.proyectocomida.plato.dto;

import es.pcomida.proyectocomida.plato.models.Categoria;
import es.pcomida.proyectocomida.plato.models.Pais;
import es.pcomida.proyectocomida.plato.models.Tipo;
import es.pcomida.proyectocomida.plato.models.Variante;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatoUpdateDto {

    private String nombre;
    private String descripcion;
    private Variante variante;
    private Categoria categoria;
    private Pais pais;
    private Tipo tipo;
    private Boolean isPremium;

    @Positive(message = "El precio debe ser mayor que 0")
    private Double precio;

    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    @URL(message = "La imagen debe ser una URL válida")
    @Size(max = 500, message = "La URL de la imagen no puede superar 500 caracteres")
    private String imageUrl;
}