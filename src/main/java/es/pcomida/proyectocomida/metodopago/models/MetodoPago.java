package es.pcomida.proyectocomida.metodopago.models;

import es.pcomida.proyectocomida.usuario.models.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "METODOS_PAGO")
public class MetodoPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El tipo de método de pago no puede ser nulo")
    @Enumerated(EnumType.STRING)
    private TipoMetodoPago tipo;

    @NotBlank(message = "El número de tarjeta no puede estar vacío")
    @Column(nullable = false)
    private String numeroTarjeta;

    @NotBlank(message = "La fecha de expiración no puede estar vacía")
    @Column(nullable = false)
    private String fechaExpiracion;

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDefault = false;

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;
}
