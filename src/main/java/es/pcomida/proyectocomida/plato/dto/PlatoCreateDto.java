package es.pcomida.proyectocomida.plato.dto;

import es.pcomida.proyectocomida.plato.models.Categoria;
import es.pcomida.proyectocomida.plato.models.Pais;
import es.pcomida.proyectocomida.plato.models.Tipo;
import es.pcomida.proyectocomida.plato.models.Variante;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Builder
@Data
public class PlatoCreateDto {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotNull(message = "El tipo no puede ser nulo")
    private Tipo tipo;

    @NotNull(message = "La categoría no puede ser nula")
    private Categoria categoria;

    @NotNull(message = "El país no puede ser nulo")
    private Pais pais;

    @NotNull(message = "La variante no puede ser nula")
    private Variante variante;

    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser mayor que 0")
    private Double precio;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    private boolean isPremium;

    @URL(message = "La imagen debe ser una URL válida")
    @Size(max = 500, message = "La URL de la imagen no puede superar 500 caracteres")
    private String imageUrl;
}