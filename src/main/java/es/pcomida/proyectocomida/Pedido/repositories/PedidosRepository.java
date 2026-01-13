package es.pcomida.proyectocomida.Pedido.repositories;

import es.pcomida.proyectocomida.Pedido.models.Estado;
import es.pcomida.proyectocomida.Pedido.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PedidosRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuario(Long usuario);

    List<Pedido> findAllByUsuarioAndEstado(Long usuario, Estado estado);

    @Query("SELECT p FROM Pedido p WHERE LOWER(p.estado) LIKE %:estado%")
    List<Pedido> findByEstadoContainsIgnoreCase(Estado estado);

    List<Pedido> findByUsuarioContainsIgnoreCaseAndIsDeletedFalse(Long usuario);

    @Query("SELECT p FROM Pedido p WHERE p.usuario = :usuario AND LOWER(p.estado) like %:estado%")
    List<Pedido> findByUsuarioAndEstadoContainsIgnoreCase(Long Usuario, Estado estado);

    List<Pedido> findByUsuarioAndEstadoContainsIgnoreCaseAndIsDeletedFalse(Long usuario, Estado estado);

    @Modifying
    @Query("UPDATE Pedido p SET p.isDeleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(Long id);

}