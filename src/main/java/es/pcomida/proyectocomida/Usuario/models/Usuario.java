package es.pcomida.proyectocomida.Usuario.models;

import es.pcomida.proyectocomida.Carrito.models.Carrito;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;
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

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "Los apellidos no pueden estar vacío")
    private String apellidos;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "El username no puede estar vacío")
    private String username;

    @Column(unique = true, nullable = false)
    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @NotBlank(message = "EL telefono no puede estar vacio")
    @Column(unique = true, nullable = false)
    private String telefono;

    @NotBlank(message = "La password no puede estar vacía")
    @Length(min = 10, message = "La Password debe tener al menos 5 caracteres")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String direccion;

    @NotBlank(message = "El código postal no puede estar vacío")
    @Length(min = 5, max = 5, message = "El código postal debe tener 5 dígitos")
    private String codigoPostal;

    @NotBlank(message = "La ciudad no puede estar vacía")
    private String ciudad;

    @NotBlank(message = "El país no puede estar vacío")
    private String pais;

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
    // CORRECCIÓN: Forzamos el nombre de la tabla y la columna de unión
    @CollectionTable(name = "USUARIOS_ROLES", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "rol") // Nombramos la columna que contendrá el rol
    private Set<Roles> roles;

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isSuscriptor = false;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Carrito> carritos;
}
