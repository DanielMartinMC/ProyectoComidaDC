package es.pcomida.proyectocomida.Usuario.repositories;

import es.pcomida.proyectocomida.Usuario.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByIdAndIsDeletedFalse(Long id);

    Optional<Usuario> findByUsernameAndNombreAndApellidos(String username,String email, String apellidos);

    List<Usuario> findByCodigoPostal(String codigoPostal);

    //Email
    Optional <Usuario> findByEmailAndIsDeletedFalse(String email);
    Boolean existsByEmail(String email);

    //Username
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByIdAndUsername(Long id,String username);
    Boolean existsByUsername(String username);

    List<Usuario> findByIsSuscriptorTrue();

    //IsDeleted
    List<Usuario> findAllByIsDeletedFalse();
    List<Usuario> findAllByIsDeletedTrue();


}
