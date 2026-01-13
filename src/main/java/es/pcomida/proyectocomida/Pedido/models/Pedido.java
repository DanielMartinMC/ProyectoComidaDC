package es.pcomida.proyectocomida.Pedido.models;

import es.pcomida.proyectocomida.Plato.models.Plato;
import es.pcomida.proyectocomida.Plato.models.Tipo;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PEDIDOS")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date fechaPedido;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private Long usuario;

    @Column(nullable = false)
    private String direccion;

    @Builder.Default
    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "id_plato")
    private Plato plato;
}
