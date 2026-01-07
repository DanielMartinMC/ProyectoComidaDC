package es.pcomida.proyectocomida.Carrito.models;

import es.pcomida.proyectocomida.Carrito_item.models.Carrito_item;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CARRITOS")
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID usuario;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Estados estado;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime fechaCreación = LocalDateTime.now();

    @Column(nullable = false)
    private Float cupon;

    @Column(nullable = false)
    @Builder.Default
    private Float descuento = 0.0f;

    @Column(nullable = false)
    private Float ImpuestosCalc;

    @Column(nullable = false)
    private Double total;

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL)
    private List<Carrito_item> items = new ArrayList<>();



}
