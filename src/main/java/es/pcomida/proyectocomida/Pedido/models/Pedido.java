package es.pcomida.proyectocomida.Pedido.models;

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
@Table(name = "PEDIDOS")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date fechaPedido;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private Long usuario;

    @Column(nullable = false)
    private String direccion;
}
