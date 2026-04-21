package es.pcomida.proyectocomida.carritoitem.repositories;

import es.pcomida.proyectocomida.carritoitem.models.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    Optional<CarritoItem> findByCarritoIdAndPlatoId(Long carritoId, Long platoId);
}
