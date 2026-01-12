package es.pcomida.proyectocomida.Usuario.models;

import es.pcomida.proyectocomida.Carrito.models.Carrito;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USUARIOS")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "nombre no puede estar vacío")
    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "apellidos no puede estar vacío")
    private String apellidos;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username no puede estar vacío")
    private String username;

    @Column(unique = true, nullable = false)
    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    @NotBlank(message = "Email no puede estar vacío")
    private String email;

    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 10, message = "Password debe tener al menos 5 caracteres")
    @Column(nullable = false)
    private String password;

    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDeleted = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles;

    @OneToMany
    @JoinColumn(name = "carrito_id")
    private Carrito titular;
}
