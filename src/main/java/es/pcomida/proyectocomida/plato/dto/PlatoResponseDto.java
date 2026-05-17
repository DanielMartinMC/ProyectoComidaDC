package es.pcomida.proyectocomida.plato.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.pcomida.proyectocomida.plato.models.Categoria;
import es.pcomida.proyectocomida.plato.models.Pais;
import es.pcomida.proyectocomida.plato.models.Tipo;
import es.pcomida.proyectocomida.plato.models.Variante;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatoResponseDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private Tipo tipo;
    private Categoria categoria;
    private Pais pais;
    private Variante variante;
    private Double precio;
    private Integer cantidad;
    private String imageUrl;

    @JsonProperty("isPremium")
    private boolean isPremium;
}