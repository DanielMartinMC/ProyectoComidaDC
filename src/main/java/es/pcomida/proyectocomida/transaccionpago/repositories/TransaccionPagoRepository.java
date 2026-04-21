package es.pcomida.proyectocomida.transaccionpago.repositories;

import es.pcomida.proyectocomida.transaccionpago.models.TransaccionPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransaccionPagoRepository extends JpaRepository<TransaccionPago, Long>, JpaSpecificationExecutor<TransaccionPago> {
    Optional<TransaccionPago> findByPedidoId(Long pedidoId);
}
