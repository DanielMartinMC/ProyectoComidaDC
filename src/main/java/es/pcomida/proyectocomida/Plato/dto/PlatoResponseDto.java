package es.pcomida.proyectocomida.Plato.dto;

import es.pcomida.proyectocomida.Plato.models.Categoria;
import es.pcomida.proyectocomida.Plato.models.Tipo;
import es.pcomida.proyectocomida.Plato.models.Variante;
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
    private Variante variante;
    private Double precio;
    private Integer cantidad;
}
