package es.pcomida.proyectocomida.pedido.services;

import es.pcomida.proyectocomida.carrito.exceptions.CarritoNotFoundException;
import es.pcomida.proyectocomida.carrito.models.Carrito;
import es.pcomida.proyectocomida.carrito.models.Estados;
import es.pcomida.proyectocomida.carrito.repositories.CarritoRepository;
import es.pcomida.proyectocomida.pedido.dto.CheckoutRequestDto;
import es.pcomida.proyectocomida.pedido.dto.PedidoResponseDto;
import es.pcomida.proyectocomida.pedido.dto.PedidoUpdateDto;
import es.pcomida.proyectocomida.pedido.exceptions.PedidoNotFoundException;
import es.pcomida.proyectocomida.pedido.mapper.PedidoMapper;
import es.pcomida.proyectocomida.pedido.models.Estado;
import es.pcomida.proyectocomida.pedido.models.Pedido;
import es.pcomida.proyectocomida.pedido.repositories.PedidosRepository;
import es.pcomida.proyectocomida.usuario.models.Roles;
import es.pcomida.proyectocomida.usuario.models.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidosServiceImplTest {

    // -----------------------------------------------------------------------
    // Mocks
    // -----------------------------------------------------------------------
    @Mock private PedidosRepository pedidosRepository;
    @Mock private PedidoMapper pedidoMapper;
    @Mock private CarritoRepository carritoRepository;
    @Mock private SecurityContext securityContext;
    @Mock private Authentication authentication;

    @InjectMocks
    private PedidosServiceImpl pedidosService;

    // -----------------------------------------------------------------------
    // Fixtures reutilizables
    // -----------------------------------------------------------------------
    private Usuario usuarioPropietario;
    private Usuario usuarioAjeno;
    private Usuario usuarioAdmin;

    @BeforeEach
    void setUp() {
        // Usuario normal dueño del recurso
        usuarioPropietario = Usuario.builder()
                .id(1L)
                .nombre("Carlos")
                .apellidos("García")
                .username("carlosg")
                .email("carlos@test.com")
                .telefono("600000001")
                .password("password1234")
                .roles(Set.of(Roles.USER))
                .isSuscriptor(false)
                .isDeleted(false)
                .build();

        // Usuario normal que NO es dueño
        usuarioAjeno = Usuario.builder()
                .id(2L)
                .nombre("Ana")
                .apellidos("López")
                .username("anal")
                .email("ana@test.com")
                .telefono("600000002")
                .password("password1234")
                .roles(Set.of(Roles.USER))
                .isSuscriptor(false)
                .isDeleted(false)
                .build();

        // Admin
        usuarioAdmin = Usuario.builder()
                .id(3L)
                .nombre("Admin")
                .apellidos("Root")
                .username("adminroot")
                .email("admin@test.com")
                .telefono("600000003")
                .password("password1234")
                .roles(Set.of(Roles.ADMIN))
                .isSuscriptor(false)
                .isDeleted(false)
                .build();

        // Configuramos el SecurityContext para que devuelva el usuario propietario por defecto
        SecurityContextHolder.setContext(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getPrincipal()).thenReturn(usuarioPropietario);
    }

    // -----------------------------------------------------------------------
    // findById
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("findById: devuelve el DTO cuando el pedido existe y el usuario es el dueño")
    void findById_whenOwner_returnsDto() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .id(10L)
                .usuario(usuarioPropietario)
                .estado(Estado.EnProceso)
                .total(50.0)
                .direccion("Calle Mayor 1")
                .fechaPedido(new Date())
                .isDeleted(false)
                .build();

        PedidoResponseDto expectedDto = PedidoResponseDto.builder()
                .id(10L)
                .estado(Estado.EnProceso)
                .total(50.0)
                .usuario(usuarioPropietario)
                .build();

        when(pedidosRepository.findById(10L)).thenReturn(Optional.of(pedido));
        when(pedidoMapper.toPedidoResponseDto(pedido)).thenReturn(expectedDto);

        // Act
        PedidoResponseDto result = pedidosService.findById(10L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getEstado()).isEqualTo(Estado.EnProceso);
        verify(pedidosRepository).findById(10L);
    }

    @Test
    @DisplayName("findById: lanza PedidoNotFoundException cuando el pedido no existe")
    void findById_whenNotFound_throwsPedidoNotFoundException() {
        // Arrange
        when(pedidosRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> pedidosService.findById(99L))
                .isInstanceOf(PedidoNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("findById: lanza AccessDeniedException cuando el usuario no es el dueño")
    void findById_whenNotOwner_throwsAccessDeniedException() {
        // Arrange — el pedido pertenece a usuarioPropietario, pero el usuario autenticado es usuarioAjeno
        when(authentication.getPrincipal()).thenReturn(usuarioAjeno);

        Pedido pedido = Pedido.builder()
                .id(10L)
                .usuario(usuarioPropietario)  // dueño = propietario, no el usuario autenticado
                .estado(Estado.EnProceso)
                .total(50.0)
                .direccion("Calle Mayor 1")
                .fechaPedido(new Date())
                .build();

        when(pedidosRepository.findById(10L)).thenReturn(Optional.of(pedido));

        // Act + Assert
        assertThatThrownBy(() -> pedidosService.findById(10L))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("findById: el ADMIN puede ver pedidos de cualquier usuario")
    void findById_whenAdmin_returnsDto() {
        // Arrange — usuario autenticado es ADMIN
        when(authentication.getPrincipal()).thenReturn(usuarioAdmin);

        Pedido pedido = Pedido.builder()
                .id(10L)
                .usuario(usuarioPropietario)
                .estado(Estado.Completado)
                .total(99.0)
                .direccion("Calle Mayor 1")
                .fechaPedido(new Date())
                .build();

        PedidoResponseDto expectedDto = PedidoResponseDto.builder()
                .id(10L)
                .estado(Estado.Completado)
                .total(99.0)
                .build();

        when(pedidosRepository.findById(10L)).thenReturn(Optional.of(pedido));
        when(pedidoMapper.toPedidoResponseDto(pedido)).thenReturn(expectedDto);

        // Act
        PedidoResponseDto result = pedidosService.findById(10L);

        // Assert
        assertThat(result.getEstado()).isEqualTo(Estado.Completado);
    }

    // -----------------------------------------------------------------------
    // save (checkout)
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("save (checkout): crea un pedido correctamente cuando el carrito tiene items")
    void save_whenCarritoHasItems_createsPedido() {
        // Arrange
        Carrito carrito = buildCarritoConItems(usuarioPropietario, 2);
        carrito.setTotal(40.0);

        CheckoutRequestDto checkoutDto = new CheckoutRequestDto();
        checkoutDto.setCarritoId(5L);

        Pedido nuevoPedido = Pedido.builder()
                .id(20L)
                .usuario(usuarioPropietario)
                .estado(Estado.EnProceso)
                .total(40.0)
                .direccion(usuarioPropietario.getDireccion())
                .fechaPedido(new Date())
                .build();

        PedidoResponseDto responseDto = PedidoResponseDto.builder()
                .id(20L)
                .estado(Estado.EnProceso)
                .total(40.0)
                .build();

        when(carritoRepository.findById(5L)).thenReturn(Optional.of(carrito));
        when(pedidoMapper.toPedido(carrito)).thenReturn(nuevoPedido);
        when(pedidosRepository.save(any(Pedido.class))).thenReturn(nuevoPedido);
        when(pedidoMapper.toPedidoResponseDto(nuevoPedido)).thenReturn(responseDto);

        // Act
        PedidoResponseDto result = pedidosService.save(checkoutDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(20L);
        assertThat(result.getEstado()).isEqualTo(Estado.EnProceso);

        // El estado del carrito debe marcarse como Contenido
        assertThat(carrito.getEstado()).isEqualTo(Estados.Contenido);
        verify(carritoRepository).save(carrito);
        verify(pedidosRepository).save(nuevoPedido);
    }

    @Test
    @DisplayName("save (checkout): lanza CarritoNotFoundException cuando el carrito no existe")
    void save_whenCarritoNotFound_throwsCarritoNotFoundException() {
        // Arrange
        CheckoutRequestDto checkoutDto = new CheckoutRequestDto();
        checkoutDto.setCarritoId(99L);

        when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> pedidosService.save(checkoutDto))
                .isInstanceOf(CarritoNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("save (checkout): lanza IllegalStateException cuando el carrito está vacío")
    void save_whenCarritoIsEmpty_throwsIllegalStateException() {
        // Arrange
        Carrito carritoVacio = Carrito.builder()
                .id(5L)
                .usuario(usuarioPropietario)
                .estado(Estados.Vacio)
                .items(new ArrayList<>())
                .total(0.0)
                .descuento(0.0)
                .impuestosCalc(0.0)
                .isDeleted(false)
                .build();

        CheckoutRequestDto checkoutDto = new CheckoutRequestDto();
        checkoutDto.setCarritoId(5L);

        when(carritoRepository.findById(5L)).thenReturn(Optional.of(carritoVacio));

        // Act + Assert
        assertThatThrownBy(() -> pedidosService.save(checkoutDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("vacío");
    }

    @Test
    @DisplayName("save (checkout): lanza AccessDeniedException si el carrito pertenece a otro usuario")
    void save_whenCarritoNotOwned_throwsAccessDeniedException() {
        // Arrange — carrito pertenece a usuarioAjeno, pero el autenticado es usuarioPropietario
        Carrito carritoAjeno = buildCarritoConItems(usuarioAjeno, 1);

        CheckoutRequestDto checkoutDto = new CheckoutRequestDto();
        checkoutDto.setCarritoId(5L);

        when(carritoRepository.findById(5L)).thenReturn(Optional.of(carritoAjeno));

        // Act + Assert
        assertThatThrownBy(() -> pedidosService.save(checkoutDto))
                .isInstanceOf(AccessDeniedException.class);
    }

    // -----------------------------------------------------------------------
    // update
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("update: actualiza el estado de un pedido correctamente")
    void update_whenOwner_updatesEstado() {
        // Arrange
        Pedido pedidoActual = Pedido.builder()
                .id(10L)
                .usuario(usuarioPropietario)
                .estado(Estado.EnProceso)
                .total(50.0)
                .direccion("Calle Mayor 1")
                .fechaPedido(new Date())
                .build();

        PedidoUpdateDto updateDto = PedidoUpdateDto.builder()
                .estado(Estado.Enviado)
                .build();

        Pedido pedidoActualizado = Pedido.builder()
                .id(10L)
                .usuario(usuarioPropietario)
                .estado(Estado.Enviado)
                .total(50.0)
                .direccion("Calle Mayor 1")
                .fechaPedido(pedidoActual.getFechaPedido())
                .build();

        PedidoResponseDto responseDto = PedidoResponseDto.builder()
                .id(10L)
                .estado(Estado.Enviado)
                .total(50.0)
                .build();

        when(pedidosRepository.findById(10L)).thenReturn(Optional.of(pedidoActual));
        when(pedidoMapper.toPedido(updateDto, pedidoActual)).thenReturn(pedidoActualizado);
        when(pedidosRepository.save(pedidoActualizado)).thenReturn(pedidoActualizado);
        when(pedidoMapper.toPedidoResponseDto(pedidoActualizado)).thenReturn(responseDto);

        // Act
        PedidoResponseDto result = pedidosService.update(10L, updateDto);

        // Assert
        assertThat(result.getEstado()).isEqualTo(Estado.Enviado);
        verify(pedidosRepository).save(pedidoActualizado);
    }

    @Test
    @DisplayName("update: lanza PedidoNotFoundException si el pedido no existe")
    void update_whenNotFound_throwsPedidoNotFoundException() {
        // Arrange
        when(pedidosRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> pedidosService.update(99L, PedidoUpdateDto.builder().build()))
                .isInstanceOf(PedidoNotFoundException.class);
    }

    @Test
    @DisplayName("update: lanza AccessDeniedException si el usuario no es el dueño")
    void update_whenNotOwner_throwsAccessDeniedException() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(usuarioAjeno);

        Pedido pedido = Pedido.builder()
                .id(10L)
                .usuario(usuarioPropietario)
                .estado(Estado.EnProceso)
                .total(50.0)
                .direccion("Calle Mayor 1")
                .fechaPedido(new Date())
                .build();

        when(pedidosRepository.findById(10L)).thenReturn(Optional.of(pedido));

        // Act + Assert
        assertThatThrownBy(() -> pedidosService.update(10L, PedidoUpdateDto.builder().build()))
                .isInstanceOf(AccessDeniedException.class);
    }

    // -----------------------------------------------------------------------
    // deleteById (soft delete)
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("deleteById: hace soft delete correctamente cuando el usuario es el dueño")
    void deleteById_whenOwner_setsIsDeletedTrue() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .id(10L)
                .usuario(usuarioPropietario)
                .estado(Estado.EnProceso)
                .total(50.0)
                .direccion("Calle Mayor 1")
                .fechaPedido(new Date())
                .isDeleted(false)
                .build();

        when(pedidosRepository.findById(10L)).thenReturn(Optional.of(pedido));

        // Act
        pedidosService.deleteById(10L);

        // Assert
        assertThat(pedido.getIsDeleted()).isTrue();
        verify(pedidosRepository).save(pedido);
    }

    @Test
    @DisplayName("deleteById: lanza PedidoNotFoundException si el pedido no existe")
    void deleteById_whenNotFound_throwsPedidoNotFoundException() {
        // Arrange
        when(pedidosRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> pedidosService.deleteById(99L))
                .isInstanceOf(PedidoNotFoundException.class);
    }

    @Test
    @DisplayName("deleteById: lanza AccessDeniedException si el usuario no es el dueño")
    void deleteById_whenNotOwner_throwsAccessDeniedException() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(usuarioAjeno);

        Pedido pedido = Pedido.builder()
                .id(10L)
                .usuario(usuarioPropietario)
                .estado(Estado.EnProceso)
                .total(50.0)
                .direccion("Calle Mayor 1")
                .fechaPedido(new Date())
                .build();

        when(pedidosRepository.findById(10L)).thenReturn(Optional.of(pedido));

        // Act + Assert
        assertThatThrownBy(() -> pedidosService.deleteById(10L))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("deleteById: el ADMIN puede borrar pedidos de cualquier usuario")
    void deleteById_whenAdmin_setsIsDeletedTrue() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(usuarioAdmin);

        Pedido pedido = Pedido.builder()
                .id(10L)
                .usuario(usuarioPropietario)
                .estado(Estado.EnProceso)
                .total(50.0)
                .direccion("Calle Mayor 1")
                .fechaPedido(new Date())
                .isDeleted(false)
                .build();

        when(pedidosRepository.findById(10L)).thenReturn(Optional.of(pedido));

        // Act
        pedidosService.deleteById(10L);

        // Assert
        assertThat(pedido.getIsDeleted()).isTrue();
        verify(pedidosRepository).save(pedido);
    }

    // -----------------------------------------------------------------------
    // Helpers privados
    // -----------------------------------------------------------------------

    private Carrito buildCarritoConItems(Usuario usuario, int numItems) {
        List<es.pcomida.proyectocomida.carritoitem.models.CarritoItem> items = new ArrayList<>();
        Carrito carrito = Carrito.builder()
                .id(5L)
                .usuario(usuario)
                .estado(Estados.Contenido)
                .items(items)
                .total(numItems * 10.0)
                .descuento(0.0)
                .impuestosCalc(0.0)
                .isDeleted(false)
                .build();

        for (int i = 0; i < numItems; i++) {
            es.pcomida.proyectocomida.plato.models.Plato plato =
                    es.pcomida.proyectocomida.plato.models.Plato.builder()
                            .id((long) (i + 1))
                            .nombre("Plato " + i)
                            .precio(10.0)
                            .build();

            es.pcomida.proyectocomida.carritoitem.models.CarritoItem item =
                    es.pcomida.proyectocomida.carritoitem.models.CarritoItem.builder()
                            .id((long) (i + 1))
                            .plato(plato)
                            .carrito(carrito)
                            .cantidad(1)
                            .precioUnitario(10.0)
                            .build();
            items.add(item);
        }
        return carrito;
    }
}