package es.pcomida.proyectocomida.metodopago.repositories;

import es.pcomida.proyectocomida.metodopago.models.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {
    List<MetodoPago> findByUsuarioIdAndIsDeletedFalse(Long usuarioId);
    Optional<MetodoPago> findByIdAndUsuarioIdAndIsDeletedFalse(Long id, Long usuarioId);

    @Modifying
    @Query("UPDATE MetodoPago mp SET mp.isDefault = FALSE WHERE mp.usuario.id = :usuarioId AND mp.isDefault = TRUE")
    void unsetAllDefaultMetodosPagoForUser(Long usuarioId);
}
