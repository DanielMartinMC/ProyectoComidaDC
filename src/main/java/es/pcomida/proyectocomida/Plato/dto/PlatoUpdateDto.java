package es.pcomida.proyectocomida.Plato.dto;

import es.pcomida.proyectocomida.Plato.models.Categoria;
import es.pcomida.proyectocomida.Plato.models.Tipo;
import es.pcomida.proyectocomida.Plato.models.Variante;
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
    private Double precio;
    private Integer cantidad;
}
