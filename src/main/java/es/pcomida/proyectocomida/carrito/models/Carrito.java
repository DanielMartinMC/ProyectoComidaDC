package es.pcomida.proyectocomida.carrito.models;

import es.pcomida.proyectocomida.carritoitem.models.CarritoItem;
import es.pcomida.proyectocomida.usuario.models.Usuario;
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
    private List<CarritoItem> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    // Método helper para recalcular totales
    public void recalcularTotales() {
        double subtotal = items.stream()
                .mapToDouble(item -> item.getPlato().getPrecio() * item.getCantidad())
                .sum();

        // Aplicar descuento de suscriptor si el usuario lo es
        if (this.usuario != null && this.usuario.getIsSuscriptor()) {
            subtotal *= 0.85; // Aplica un 15% de descuento
        }

        // Aplicar descuento por cupón si existe
        if (this.descuento != null) {
            subtotal = Math.max(0.0, subtotal - this.descuento);
        }

        this.total = subtotal;
        
        // Calcular impuestos (ejemplo 10%)
        this.impuestosCalc = this.total * 0.10; 
    }
}
