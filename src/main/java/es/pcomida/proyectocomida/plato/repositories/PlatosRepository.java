package es.pcomida.proyectocomida.plato.repositories;

import es.pcomida.proyectocomida.plato.models.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatosRepository extends JpaRepository<Plato, Long>, JpaSpecificationExecutor<Plato> {

    @Modifying
    @Query("UPDATE Plato p SET p.isDeleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(Long id);
}
