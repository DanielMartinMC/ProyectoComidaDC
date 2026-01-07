package es.pcomida.proyectocomida.Carrito_item.models;

import es.pcomida.proyectocomida.Carrito.models.Carrito;
import es.pcomida.proyectocomida.Plato.models.Plato;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carrito_items")
public class Carrito_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plato_id")
    private Plato plato;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;

    @Column(nullable = false)
    private Integer cantidad;


}
