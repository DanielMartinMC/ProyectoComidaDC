package es.pcomida.proyectocomida.Carrito.repositories;

import es.pcomida.proyectocomida.Carrito.dto.CarritoResponseDto;
import es.pcomida.proyectocomida.Carrito.models.Carrito;
import es.pcomida.proyectocomida.Carrito.models.Estados;
import es.pcomida.proyectocomida.Plato.models.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    //Usuario findByUsuario(Long usuario);

    Optional<Carrito> findByUsuarioIdAndEstado(Long usuarioId, Estados estado);

    @Query("SELECT c FROM Carrito c WHERE c.usuario = :usuario AND LOWER(c.estado) LIKE %:estado%")
    List<Carrito> findByUsuarioAndEstadoContainsIgnoreCase(UUID usuario, Estados estado);


    List<Carrito> findByUsuario(UUID usuario);

    List<Carrito> findByEstado(Estados  estado);

    @Query("SELECT c FROM Carrito c WHERE LOWER(c.estado) LIKE %:estado%")
    List<Carrito> findByEstadoContainingIgnoreCase(Estados estado);


    Optional<CarritoResponseDto> findByEstadoAndFechaCreación(Estados estado, LocalDateTime fechaCreación);




}
