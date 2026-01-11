package es.pcomida.proyectocomida.Carrito_item.repositories;

import es.pcomida.proyectocomida.Carrito_item.models.Carrito_item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Carrito_itemRepository extends JpaRepository<Carrito_item, Long> {

    Optional<Carrito_item> findByCarritoIdAndPlatoId(Long plato_id, Long carrito_id);



}
