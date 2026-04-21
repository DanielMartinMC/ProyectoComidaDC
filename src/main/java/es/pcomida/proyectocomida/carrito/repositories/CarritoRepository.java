package es.pcomida.proyectocomida.carrito.repositories;

import es.pcomida.proyectocomida.carrito.models.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long>, JpaSpecificationExecutor<Carrito> {
    Optional<Carrito> findByUsuarioIdAndIsDeletedFalse(Long usuarioId);
}
