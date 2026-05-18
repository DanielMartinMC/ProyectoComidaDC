package es.pcomida.proyectocomida.transaccionpago.models;

import es.pcomida.proyectocomida.pedido.models.Pedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSACCIONES_PAGO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency; // Ej: "EUR"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago status;

    @Column(columnDefinition = "TEXT")
    private String paymentDetails; // Para IDs de transacciones externas (Stripe)

    private LocalDateTime confirmationDate;
}
