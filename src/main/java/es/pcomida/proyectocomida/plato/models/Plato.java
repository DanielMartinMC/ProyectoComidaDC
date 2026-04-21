package es.pcomida.proyectocomida.plato.models;


import jakarta.persistence.*;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PLATOS")
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Variante variante;

    @Column(nullable = false)
    private Integer cantidad;

    @Builder.Default
    @Column(nullable = false)
    private boolean isDeleted = false;

}
