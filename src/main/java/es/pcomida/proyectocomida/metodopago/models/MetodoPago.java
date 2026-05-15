package es.pcomida.proyectocomida.metodopago.models;

import es.pcomida.proyectocomida.usuario.models.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder @ToString @Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "METODOS_PAGO")
public class MetodoPago {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @Enumerated(EnumType.STRING)
    private TipoMetodoPago tipo;

    @NotBlank @Column(nullable = false)
    private String numeroTarjeta;

    @NotBlank @Column(nullable = false)
    private String fechaExpiracion;

    @Column(columnDefinition = "boolean default false") @Builder.Default
    private Boolean isDefault = false;

    @Column(nullable = false, precision = 10, scale = 2) @Builder.Default
    private BigDecimal saldoDisponible = new BigDecimal("100.00");

    @Column(columnDefinition = "boolean default false") @Builder.Default
    private Boolean isDeleted = false;

    @CreationTimestamp @Column(updatable = false, nullable = false) @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp @Column(nullable = false) @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;
}