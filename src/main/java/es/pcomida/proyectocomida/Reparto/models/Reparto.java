package es.pcomida.proyectocomida.Reparto.models;

import es.pcomida.proyectocomida.Carrito.models.Carrito;
import es.pcomida.proyectocomida.Pedido.models.Pedido;
import es.pcomida.proyectocomida.Usuario.models.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REPARTOS")
public class Reparto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Long repartidorID;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido_id;

    @Column(nullable = false)
    private String direccionDestino;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String telefonoContacto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RepartoEstado estado = RepartoEstado.PENDIENTE;


}
