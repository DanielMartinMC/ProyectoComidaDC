package es.pcomida.proyectocomida.metodopago.services;

import es.pcomida.proyectocomida.metodopago.dto.MetodoPagoCreateDto;
import es.pcomida.proyectocomida.metodopago.dto.MetodoPagoDto;
import es.pcomida.proyectocomida.metodopago.mapper.MetodoPagoMapper;
import es.pcomida.proyectocomida.metodopago.models.MetodoPago;
import es.pcomida.proyectocomida.metodopago.models.TipoMetodoPago;
import es.pcomida.proyectocomida.metodopago.repositories.MetodoPagoRepository;
import es.pcomida.proyectocomida.usuario.models.Usuario;
import es.pcomida.proyectocomida.usuario.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetodoPagoServiceImplTest {

    @Mock
    private MetodoPagoRepository metodoPagoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MetodoPagoMapper metodoPagoMapper;

    @InjectMocks
    private MetodoPagoServiceImpl metodoPagoService;

    @Test
    void createMetodoPago_ShouldCreateAndReturnMetodoPagoDto() {
        // Arrange
        Usuario usuario = Usuario.builder().id(1L).metodosPago(new ArrayList<>()).build();
        MetodoPagoCreateDto createDto = MetodoPagoCreateDto.builder()
                .tipo(TipoMetodoPago.TARJETA_CREDITO)
                .numeroTarjeta("1234567890123456")
                .fechaExpiracion("12/25")
                .isDefault(true)
                .build();

        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setId(1L);
        metodoPago.setNumeroTarjeta("************3456");

        MetodoPagoDto expectedDto = MetodoPagoDto.builder()
                .id(1L)
                .numeroTarjeta("************3456")
                .build();

        // Mocking the behavior of the dependencies
        when(metodoPagoMapper.toMetodoPago(any(MetodoPagoCreateDto.class))).thenReturn(metodoPago);
        when(metodoPagoRepository.save(any(MetodoPago.class))).thenReturn(metodoPago);
        when(metodoPagoMapper.toMetodoPagoDto(any(MetodoPago.class))).thenReturn(expectedDto);

        // Act
        MetodoPagoDto result = metodoPagoService.createMetodoPago(createDto, usuario);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getNumeroTarjeta(), result.getNumeroTarjeta());

        // Verify that the mocks were called
        verify(metodoPagoRepository).unsetAllDefaultMetodosPagoForUser(usuario.getId());
        verify(metodoPagoRepository).save(any(MetodoPago.class));
        verify(usuarioRepository).save(any(Usuario.class));
        verify(metodoPagoMapper).toMetodoPago(any(MetodoPagoCreateDto.class));
        verify(metodoPagoMapper).toMetodoPagoDto(any(MetodoPago.class));
    }
}
