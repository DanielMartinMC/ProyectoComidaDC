package es.pcomida.proyectocomida.carrito.services;

import es.pcomida.proyectocomida.carrito.dto.CarritoCreateDto;
import es.pcomida.proyectocomida.carrito.dto.CarritoResponseDTO;
import es.pcomida.proyectocomida.carrito.dto.CarritoUpdateDto;
import es.pcomida.proyectocomida.carrito.exceptions.CarritoNotFoundException;
import es.pcomida.proyectocomida.carrito.mapper.CarritoMapper;
import es.pcomida.proyectocomida.carrito.models.Carrito;
import es.pcomida.proyectocomida.carrito.models.Estados;
import es.pcomida.proyectocomida.carrito.repositories.CarritoRepository;
import es.pcomida.proyectocomida.carritoitem.dto.AddCarritoItemDTO;
import es.pcomida.proyectocomida.carritoitem.models.CarritoItem;
import es.pcomida.proyectocomida.carritoitem.repositories.CarritoItemRepository;
import es.pcomida.proyectocomida.plato.exceptions.PlatoNotFoundException;
import es.pcomida.proyectocomida.plato.models.Plato;
import es.pcomida.proyectocomida.plato.repositories.PlatosRepository;
import es.pcomida.proyectocomida.usuario.models.Roles;
import es.pcomida.proyectocomida.usuario.models.Usuario;
import es.pcomida.proyectocomida.usuario.repositories.UsuarioRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceImplTest {

    // -----------------------------------------------------------------------
    // Mocks
    // -----------------------------------------------------------------------
    @Mock private CarritoMapper carritoMapper;
    @Mock private CarritoRepository carritoRepository;
    @Mock private CarritoItemRepository carritoItemRepository;
    @Mock private PlatosRepository platosRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private SecurityContext securityContext;
    @Mock private Authentication authentication;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    // -----------------------------------------------------------------------
    // Fixtures
    // -----------------------------------------------------------------------
    private Usuario usuarioPropietario;
    private Usuario usuarioAjeno;
    private Usuario usuarioAdmin;
    private Usuario usuarioSuscriptor;

    @BeforeEach
    void setUp() {
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

        usuarioSuscriptor = Usuario.builder()
                .id(4L)
                .nombre("María")
                .apellidos("Suscriptora")
                .username("mariasusc")
                .email("maria@test.com")
                .telefono("600000004")
                .password("password1234")
                .roles(Set.of(Roles.USER))
                .isSuscriptor(true)   // ← tiene descuento del 15%
                .isDeleted(false)
                .build();

        SecurityContextHolder.setContext(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getPrincipal()).thenReturn(usuarioPropietario);
    }

    // -----------------------------------------------------------------------
    // findById
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("findById: devuelve el DTO cuando el carrito existe y el usuario es el dueño")
    void findById_whenOwner_returnsDto() {
        // Arrange
        Carrito carrito = buildCarrito(1L, usuarioPropietario, Estados.Vacio);

        CarritoResponseDTO expected = CarritoResponseDTO.builder()
                .id(1L)
                .usuarioId(usuarioPropietario.getId())
                .estado(Estados.Vacio)
                .total(0.0)
                .build();

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(carritoMapper.toCarritoResponseDTO(carrito)).thenReturn(expected);

        // Act
        CarritoResponseDTO result = carritoService.findById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEstado()).isEqualTo(Estados.Vacio);
        verify(carritoRepository).findById(1L);
    }

    @Test
    @DisplayName("findById: lanza CarritoNotFoundException cuando el carrito no existe")
    void findById_whenNotFound_throwsCarritoNotFoundException() {
        // Arrange
        when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> carritoService.findById(99L))
                .isInstanceOf(CarritoNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("findById: lanza AccessDeniedException cuando el usuario no es el dueño")
    void findById_whenNotOwner_throwsAccessDeniedException() {
        // Arrange — usuario autenticado es ajeno
        when(authentication.getPrincipal()).thenReturn(usuarioAjeno);
        Carrito carrito = buildCarrito(1L, usuarioPropietario, Estados.Vacio);
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        // Act + Assert
        assertThatThrownBy(() -> carritoService.findById(1L))
                .isInstanceOf(AccessDeniedException.class);
    }

    // -----------------------------------------------------------------------
    // save (crear carrito)
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("save: crea un carrito vacío correctamente")
    void save_createsCarritoVacio() {
        // Arrange
        CarritoCreateDto createDto = CarritoCreateDto.builder()
                .usuarioId(1L)
                .build();

        Carrito nuevoCarrito = buildCarrito(1L, usuarioPropietario, Estados.Vacio);

        CarritoResponseDTO expected = CarritoResponseDTO.builder()
                .id(1L)
                .usuarioId(1L)
                .estado(Estados.Vacio)
                .total(0.0)
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioPropietario));
        when(carritoMapper.toCarrito(createDto, usuarioPropietario)).thenReturn(nuevoCarrito);
        when(carritoRepository.save(nuevoCarrito)).thenReturn(nuevoCarrito);
        when(carritoMapper.toCarritoResponseDTO(nuevoCarrito)).thenReturn(expected);

        // Act
        CarritoResponseDTO result = carritoService.save(createDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEstado()).isEqualTo(Estados.Vacio);
        assertThat(result.getTotal()).isEqualTo(0.0);
        verify(carritoRepository).save(nuevoCarrito);
    }

    @Test
    @DisplayName("save: lanza RuntimeException cuando el usuario no existe")
    void save_whenUsuarioNotFound_throwsRuntimeException() {
        // Arrange
        CarritoCreateDto createDto = CarritoCreateDto.builder().usuarioId(99L).build();
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> carritoService.save(createDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    // -----------------------------------------------------------------------
    // addPlatoToCarrito
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("addPlatoToCarrito: añade un plato nuevo al carrito correctamente")
    void addPlatoToCarrito_whenPlatoNuevo_addsItem() {
        // Arrange
        Carrito carrito = buildCarrito(1L, usuarioPropietario, Estados.Vacio);

        Plato plato = buildPlato(10L, "Paella", 12.50);

        AddCarritoItemDTO itemDto = AddCarritoItemDTO.builder()
                .platoID(10L)
                .cantidad(2)
                .build();

        CarritoResponseDTO expected = CarritoResponseDTO.builder()
                .id(1L)
                .estado(Estados.Contenido)
                .total(25.0)
                .build();

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(platosRepository.findById(10L)).thenReturn(Optional.of(plato));
        when(carritoItemRepository.findByCarritoIdAndPlatoId(1L, 10L)).thenReturn(Optional.empty());
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);
        when(carritoMapper.toCarritoResponseDTO(carrito)).thenReturn(expected);

        // Act
        CarritoResponseDTO result = carritoService.addPlatoToCarrito(1L, itemDto);

        // Assert
        assertThat(result.getEstado()).isEqualTo(Estados.Contenido);
        verify(carritoItemRepository).save(any(CarritoItem.class));
        verify(carritoRepository).save(carrito);
    }

    @Test
    @DisplayName("addPlatoToCarrito: incrementa la cantidad si el plato ya está en el carrito")
    void addPlatoToCarrito_whenPlatoDuplicado_incrementsCantidad() {
        // Arrange
        Carrito carrito = buildCarrito(1L, usuarioPropietario, Estados.Contenido);
        Plato plato = buildPlato(10L, "Paella", 12.50);

        CarritoItem itemExistente = CarritoItem.builder()
                .id(100L)
                .plato(plato)
                .carrito(carrito)
                .cantidad(1)
                .precioUnitario(12.50)
                .build();

        AddCarritoItemDTO itemDto = AddCarritoItemDTO.builder()
                .platoID(10L)
                .cantidad(3)
                .build();

        CarritoResponseDTO expected = CarritoResponseDTO.builder()
                .id(1L)
                .estado(Estados.Contenido)
                .total(50.0)
                .build();

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(platosRepository.findById(10L)).thenReturn(Optional.of(plato));
        when(carritoItemRepository.findByCarritoIdAndPlatoId(1L, 10L)).thenReturn(Optional.of(itemExistente));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);
        when(carritoMapper.toCarritoResponseDTO(carrito)).thenReturn(expected);

        // Act
        carritoService.addPlatoToCarrito(1L, itemDto);

        // Assert — la cantidad debe haber sumado: 1 existente + 3 nuevas = 4
        assertThat(itemExistente.getCantidad()).isEqualTo(4);
        verify(carritoItemRepository).save(itemExistente);
    }

    @Test
    @DisplayName("addPlatoToCarrito: lanza PlatoNotFoundException cuando el plato no existe")
    void addPlatoToCarrito_whenPlatoNotFound_throwsPlatoNotFoundException() {
        // Arrange
        Carrito carrito = buildCarrito(1L, usuarioPropietario, Estados.Vacio);
        AddCarritoItemDTO itemDto = AddCarritoItemDTO.builder().platoID(99L).cantidad(1).build();

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(platosRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> carritoService.addPlatoToCarrito(1L, itemDto))
                .isInstanceOf(PlatoNotFoundException.class);
    }

    @Test
    @DisplayName("addPlatoToCarrito: lanza AccessDeniedException si el carrito no es del usuario")
    void addPlatoToCarrito_whenNotOwner_throwsAccessDeniedException() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(usuarioAjeno);
        Carrito carrito = buildCarrito(1L, usuarioPropietario, Estados.Vacio);
        AddCarritoItemDTO itemDto = AddCarritoItemDTO.builder().platoID(10L).cantidad(1).build();

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        // Act + Assert
        assertThatThrownBy(() -> carritoService.addPlatoToCarrito(1L, itemDto))
                .isInstanceOf(AccessDeniedException.class);
    }

    // -----------------------------------------------------------------------
    // update (cupón de descuento)
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("update: aplica descuento de 10.0 con el cupón VERANO")
    void update_withCuponVERANO_appliesDescuento() {
        // Arrange
        Carrito carrito = buildCarritoConItems(1L, usuarioPropietario, 20.0);

        CarritoUpdateDto updateDto = CarritoUpdateDto.builder()
                .codigoCupon("VERANO")
                .build();

        CarritoResponseDTO expected = CarritoResponseDTO.builder()
                .id(1L)
                .descuento(10.0)
                .total(10.0)   // 20 - 10 de descuento
                .build();

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);
        when(carritoMapper.toCarritoResponseDTO(carrito)).thenReturn(expected);

        // Act
        CarritoResponseDTO result = carritoService.update(1L, updateDto);

        // Assert
        assertThat(carrito.getDescuento()).isEqualTo(10.0);
        assertThat(carrito.getCodigoCupon()).isEqualToIgnoringCase("VERANO");
        verify(carritoRepository).save(carrito);
    }

    @Test
    @DisplayName("update: pone descuento a 0.0 con un cupón inválido")
    void update_withCuponInvalido_setsDescuentoCero() {
        // Arrange
        Carrito carrito = buildCarritoConItems(1L, usuarioPropietario, 20.0);
        carrito.setDescuento(10.0); // tenía un descuento previo

        CarritoUpdateDto updateDto = CarritoUpdateDto.builder()
                .codigoCupon("INVALIDO123")
                .build();

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);
        when(carritoMapper.toCarritoResponseDTO(any())).thenReturn(new CarritoResponseDTO());

        // Act
        carritoService.update(1L, updateDto);

        // Assert — descuento debe resetearse a 0
        assertThat(carrito.getDescuento()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("update: lanza CarritoNotFoundException si el carrito no existe")
    void update_whenNotFound_throwsCarritoNotFoundException() {
        // Arrange
        when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> carritoService.update(99L, CarritoUpdateDto.builder().build()))
                .isInstanceOf(CarritoNotFoundException.class);
    }

    // -----------------------------------------------------------------------
    // deleteById (soft delete)
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("deleteById: hace soft delete correctamente")
    void deleteById_whenOwner_setsIsDeletedTrue() {
        // Arrange
        Carrito carrito = buildCarrito(1L, usuarioPropietario, Estados.Vacio);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        // Act
        carritoService.deleteById(1L);

        // Assert
        assertThat(carrito.getIsDeleted()).isTrue();
        verify(carritoRepository).save(carrito);
    }

    @Test
    @DisplayName("deleteById: lanza CarritoNotFoundException si el carrito no existe")
    void deleteById_whenNotFound_throwsCarritoNotFoundException() {
        // Arrange
        when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> carritoService.deleteById(99L))
                .isInstanceOf(CarritoNotFoundException.class);
    }

    @Test
    @DisplayName("deleteById: lanza AccessDeniedException si el usuario no es el dueño")
    void deleteById_whenNotOwner_throwsAccessDeniedException() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(usuarioAjeno);
        Carrito carrito = buildCarrito(1L, usuarioPropietario, Estados.Vacio);
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        // Act + Assert
        assertThatThrownBy(() -> carritoService.deleteById(1L))
                .isInstanceOf(AccessDeniedException.class);
    }

    // -----------------------------------------------------------------------
    // deleteItemFromCarrito
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("deleteItemFromCarrito: elimina el item y pone el carrito en Vacio si no quedan items")
    void deleteItemFromCarrito_whenLastItem_setsCarritoVacio() {
        // Arrange
        Plato plato = buildPlato(10L, "Paella", 12.50);
        Carrito carrito = buildCarrito(1L, usuarioPropietario, Estados.Contenido);

        CarritoItem item = CarritoItem.builder()
                .id(100L)
                .plato(plato)
                .carrito(carrito)
                .cantidad(1)
                .precioUnitario(12.50)
                .build();

        carrito.getItems().add(item); // único item

        when(carritoItemRepository.findById(100L)).thenReturn(Optional.of(item));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        // Act
        carritoService.deleteItemFromCarrito(1L, 100L);

        // Assert — sin items el estado debe volver a Vacio
        assertThat(carrito.getEstado()).isEqualTo(Estados.Vacio);
        assertThat(carrito.getItems()).isEmpty();
        verify(carritoItemRepository).delete(item);
        verify(carritoRepository).save(carrito);
    }

    @Test
    @DisplayName("deleteItemFromCarrito: el carrito sigue en Contenido si aún quedan items")
    void deleteItemFromCarrito_whenMoreItemsRemain_keepsCarritoContenido() {
        // Arrange
        Plato plato1 = buildPlato(10L, "Paella", 12.50);
        Plato plato2 = buildPlato(11L, "Tortilla", 8.00);
        Carrito carrito = buildCarrito(1L, usuarioPropietario, Estados.Contenido);

        CarritoItem item1 = CarritoItem.builder()
                .id(100L).plato(plato1).carrito(carrito).cantidad(1).precioUnitario(12.50).build();
        CarritoItem item2 = CarritoItem.builder()
                .id(101L).plato(plato2).carrito(carrito).cantidad(1).precioUnitario(8.00).build();

        carrito.getItems().add(item1);
        carrito.getItems().add(item2);

        when(carritoItemRepository.findById(100L)).thenReturn(Optional.of(item1));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        // Act
        carritoService.deleteItemFromCarrito(1L, 100L);

        // Assert — todavía queda item2, el estado NO debe cambiar a Vacio
        assertThat(carrito.getEstado()).isEqualTo(Estados.Contenido);
        assertThat(carrito.getItems()).hasSize(1);
    }

    @Test
    @DisplayName("deleteItemFromCarrito: lanza CarritoNotFoundException si el item pertenece a otro carrito")
    void deleteItemFromCarrito_whenItemBelongsToDifferentCarrito_throwsCarritoNotFoundException() {
        // Arrange
        Carrito otroCarrito = buildCarrito(99L, usuarioPropietario, Estados.Contenido);
        Plato plato = buildPlato(10L, "Paella", 12.50);

        CarritoItem item = CarritoItem.builder()
                .id(100L)
                .plato(plato)
                .carrito(otroCarrito)  // pertenece a otro carrito
                .cantidad(1)
                .precioUnitario(12.50)
                .build();

        when(carritoItemRepository.findById(100L)).thenReturn(Optional.of(item));

        // Act + Assert — intentamos borrarlo del carrito 1, pero el item es del carrito 99
        assertThatThrownBy(() -> carritoService.deleteItemFromCarrito(1L, 100L))
                .isInstanceOf(CarritoNotFoundException.class);
    }

    // -----------------------------------------------------------------------
    // recalcularTotales — lógica de descuento en el modelo Carrito
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("recalcularTotales: aplica el 15% de descuento para suscriptores")
    void recalcularTotales_conSuscriptor_aplicaDescuento15pct() {
        // Arrange — carrito con un item de 100€ para un usuario suscriptor
        Plato plato = buildPlato(1L, "Menú Premium", 100.0);
        Carrito carrito = buildCarrito(1L, usuarioSuscriptor, Estados.Contenido);

        CarritoItem item = CarritoItem.builder()
                .id(1L).plato(plato).carrito(carrito).cantidad(1).precioUnitario(100.0).build();
        carrito.getItems().add(item);
        carrito.setDescuento(0.0);

        // Act
        carrito.recalcularTotales();

        // Assert: 100 * 0.85 = 85.0
        assertThat(carrito.getTotal()).isEqualTo(85.0);
    }

    @Test
    @DisplayName("recalcularTotales: acumula descuento de suscriptor Y cupón")
    void recalcularTotales_conSuscriptorYCupon_aplicaAmbosDescuentos() {
        // Arrange — item de 100€, suscriptor (15%) + cupón VERANO (10€)
        Plato plato = buildPlato(1L, "Menú Premium", 100.0);
        Carrito carrito = buildCarrito(1L, usuarioSuscriptor, Estados.Contenido);

        CarritoItem item = CarritoItem.builder()
                .id(1L).plato(plato).carrito(carrito).cantidad(1).precioUnitario(100.0).build();
        carrito.getItems().add(item);
        carrito.setDescuento(10.0); // cupón VERANO

        // Act
        carrito.recalcularTotales();

        // Assert: (100 * 0.85) - 10 = 75.0
        assertThat(carrito.getTotal()).isEqualTo(75.0);
    }

    @Test
    @DisplayName("recalcularTotales: el total nunca baja de 0 con descuentos muy grandes")
    void recalcularTotales_withExcessiveDiscount_clampedToZero() {
        // Arrange — item de 5€, descuento de 100€
        Plato plato = buildPlato(1L, "Tapa", 5.0);
        Carrito carrito = buildCarrito(1L, usuarioPropietario, Estados.Contenido);

        CarritoItem item = CarritoItem.builder()
                .id(1L).plato(plato).carrito(carrito).cantidad(1).precioUnitario(5.0).build();
        carrito.getItems().add(item);
        carrito.setDescuento(100.0); // descuento mayor que el precio

        // Act
        carrito.recalcularTotales();

        // Assert — Math.max(0.0, ...) debe garantizar que no sea negativo
        assertThat(carrito.getTotal()).isGreaterThanOrEqualTo(0.0);
    }

    // -----------------------------------------------------------------------
    // Helpers privados
    // -----------------------------------------------------------------------

    private Carrito buildCarrito(Long id, Usuario usuario, Estados estado) {
        return Carrito.builder()
                .id(id)
                .usuario(usuario)
                .estado(estado)
                .items(new ArrayList<>())
                .total(0.0)
                .descuento(0.0)
                .impuestosCalc(0.0)
                .isDeleted(false)
                .build();
    }

    /** Carrito con items sintéticos para testear lógica de totales */
    private Carrito buildCarritoConItems(Long id, Usuario usuario, double precioItem) {
        Plato plato = buildPlato(10L, "Plato test", precioItem);
        Carrito carrito = buildCarrito(id, usuario, Estados.Contenido);

        CarritoItem item = CarritoItem.builder()
                .id(1L).plato(plato).carrito(carrito)
                .cantidad(1).precioUnitario(precioItem).build();

        carrito.getItems().add(item);
        carrito.setTotal(precioItem);
        return carrito;
    }

    private Plato buildPlato(Long id, String nombre, double precio) {
        return Plato.builder()
                .id(id)
                .nombre(nombre)
                .descripcion("Descripción de " + nombre)
                .precio(precio)
                .cantidad(10)
                .build();
    }
}