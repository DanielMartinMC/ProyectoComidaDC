package es.pcomida.proyectocomida.Usuario.repositories;

import es.pcomida.proyectocomida.Usuario.models.Roles;
import es.pcomida.proyectocomida.Usuario.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByIdAndIsDeletedFalse(Long id);


    //Usuario
    Optional<Usuario> findByNombreIgnoreCaseAndIsDeletedFalse(String nombre);

    @Query("select u from Usuario u where u.nombre like %:nombre% and u.apellidos like %:apellidos%")
    List<Usuario> findByNombreAndApellidosContainingIgnoreCase(String nombre, String apellidos);


    List<Usuario> findByCodigoPostal(String codigoPostal);

    //Email
    Optional <Usuario> findByEmailAndIsDeletedFalse(String email);
    Boolean existsByEmail(String email);

    //Username
    Optional<Usuario> findByUsername(String username);
    Boolean existsByUsername(String username);


    List<Usuario> findByIsSuscriptorTrue();

    //IsDeleted
    List<Usuario> findAllByIsDeletedFalse();
    List<Usuario> findAllByIsDeletedTrue();



}
