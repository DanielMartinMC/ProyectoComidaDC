package es.pcomida.proyectocomida.pedido.repositories;

import es.pcomida.proyectocomida.pedido.models.Estado;
import es.pcomida.proyectocomida.pedido.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidosRepository extends JpaRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido> {

    List<Pedido> findByUsuario(Long usuario);

    List<Pedido> findAllByUsuarioAndEstado(Long usuario, Estado estado);

    @Query("SELECT p FROM Pedido p WHERE LOWER(p.estado) LIKE %:estado%")
    List<Pedido> findByEstadoContainsIgnoreCase(Estado estado);

    List<Pedido> findByUsuarioContainsIgnoreCaseAndIsDeletedFalse(Long usuario);

    @Query("SELECT p FROM Pedido p WHERE p.usuario = :usuario AND LOWER(p.estado) like LOWER(CONCAT('%', :estado, '%'))")
    List<Pedido> findByUsuarioAndEstadoContainsIgnoreCase(Long usuario, Estado estado);

    // List<Pedido> findByUsuarioAndEstadoContainsIgnoreCaseAndIsDeletedFalse(Long usuario, Estado estado);

    @Modifying
    @Query("UPDATE Pedido p SET p.isDeleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(Long id);

    List<Pedido> findByUsuarioAndEstado(Long usuario, Estado estado);
}