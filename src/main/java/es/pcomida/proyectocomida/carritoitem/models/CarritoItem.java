package es.pcomida.proyectocomida.carritoitem.models;

import es.pcomida.proyectocomida.carrito.models.Carrito;
import es.pcomida.proyectocomida.plato.models.Plato;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CARRITO_ITEMS")
public class CarritoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plato_id", nullable = false)
    private Plato plato;

    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @Column(nullable = false)
    @Min(value = 1, message = "La cantidad no puede ser menor que 1")
    private Integer cantidad;

    // Guardamos el precio en el momento de añadirlo para evitar que cambios de precio en Plato afecten a carritos antiguos
    @Column(nullable = false)
    @Builder.Default
    private Double precioUnitario = 0.0;
}
