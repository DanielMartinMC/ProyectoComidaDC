package es.pcomida.proyectocomida.Carrito.models;

import es.pcomida.proyectocomida.Carrito_item.models.Carrito_item;
import es.pcomida.proyectocomida.Usuario.models.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Enumerated(EnumType.STRING)
    private Estados estado;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime fechaCreación = LocalDateTime.now();

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    // Cambiado de Float a String para guardar el código del cupón (ej: "VERANO2024")
    private String codigoCupon;

    // Cambiado de Float a Double para mayor precisión en precios
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    @Builder.Default
    private Double descuento = 0.0;

    // Cambiado de Float a Double
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    @Builder.Default
    private Double impuestosCalc = 0.0;

    // Total calculado y persistido (opcional, pero útil para consultas rápidas)
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    @Builder.Default
    private Double total = 0.0;

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Carrito_item> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    // Método helper para recalcular totales
    public void recalcularTotales() {
        this.total = items.stream()
                .mapToDouble(item -> item.getPlato().getPrecio() * item.getCantidad())
                .sum();
        
        // Aplicar descuento si existe
        if (this.descuento != null) {
            this.total = Math.max(0.0, this.total - this.descuento);
        }
        
        // Calcular impuestos (ejemplo 10%)
        this.impuestosCalc = this.total * 0.10; 
    }
}
