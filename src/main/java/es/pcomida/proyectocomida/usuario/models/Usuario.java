package es.pcomida.proyectocomida.usuario.models;

import es.pcomida.proyectocomida.carrito.models.Carrito;
import es.pcomida.proyectocomida.metodopago.models.MetodoPago;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USUARIOS")
public class Usuario implements UserDetails {
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
    @Column(unique = true)
    private String telefono;

    @NotBlank(message = "La password no puede estar vacía")
    @Length(min = 10, message = "La Password debe tener al menos 10 caracteres")
    @Column(nullable = false)
    private String password;

    // Opcionales en el registro — se rellenan desde el perfil
    private String direccion;
    private String codigoPostal;
    private String ciudad;
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
    @CollectionTable(name = "USUARIOS_ROLES", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "rol")
    private Set<Roles> roles;

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isSuscriptor = false;

    @Column
    private LocalDateTime suscripcionExpira;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Carrito> carritos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MetodoPago> metodosPago;

    @OneToOne
    @JoinColumn(name = "default_metodo_pago_id")
    private MetodoPago metodoPagoPorDefecto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isDeleted;
    }
}