package es.pcomida.proyectocomida.Usuario.services;

import es.pcomida.proyectocomida.Carrito.repositories.CarritoRepository;
import es.pcomida.proyectocomida.Usuario.dto.UsuarioResponseDTO;
import es.pcomida.proyectocomida.Usuario.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = {"usuarios"})
@Slf4j
@RequiredArgsConstructor
@Service
public class UsuariosServicesImpl implements UsuarioService, InitializingBean {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final CarritoRepository carritoRepository;

    @Override
    public List<UsuarioResponseDTO>

}
