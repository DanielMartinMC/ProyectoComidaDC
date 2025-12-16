package es.pcomida.proyectocomida.Plato.repositories;

import es.pcomida.proyectocomida.Plato.models.Plato;
import es.pcomida.proyectocomida.Plato.models.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlatosRepository extends JpaRepository<Plato, Long> {

    List<Plato> findByNombre(String nombre);

    List<Plato> findAllByNombreAndTipo(String nombre, Tipo tipo);

    @Query("SELECT p FROM Plato p WHERE LOWER(p.tipo) LIKE %:tipo%")
    List<Plato> findByTipoContainsIgnoreCase(Tipo tipo);

    List<Plato> findByNombreContainsIgnoreCaseAndIsDeletedFalse(String nombre);

    @Query("SELECT p FROM Plato p WHERE p.nombre = :nombre AND LOWER(p.nombre) like %:nombre% AND LOWER(p.tipo) like %:tipo%")
    List<Plato> findByNombreAndTipoContainsIgnoreCase(String nombre, Tipo tipo);

    List<Plato> findByNombreAndTipoContainsIgnoreCaseAndIsDeletedFalse(String nombre, Tipo tipo);



    

    void updateIsDeletedToTrueById(Long id);
}
