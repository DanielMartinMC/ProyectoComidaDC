package es.pcomida.proyectocomida.reparto.repositories;

import es.pcomida.proyectocomida.reparto.models.Reparto;
import es.pcomida.proyectocomida.reparto.models.RepartoEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RepartoRepository extends JpaRepository<Reparto, Long>, JpaSpecificationExecutor<Reparto> {

    Optional<Reparto>findRepartoById(Long id);
    Page<Reparto> findRepartoByUsuarioId(Long usuarioId, Pageable pageable);
    Page<Reparto> findRepartoByRepartidorID(Long repartidorId, Pageable pageable);
    Page<Reparto> findRepartoByCiudad(String ciudad, Pageable pageable);

    List<Reparto>findRepartoByRepartidorIDAndEstado(Long repartidorId, RepartoEstado estado, Pageable pageable);

    boolean existsByRepartidorIDAndEstado(Long repartidorId, RepartoEstado estado);



}
