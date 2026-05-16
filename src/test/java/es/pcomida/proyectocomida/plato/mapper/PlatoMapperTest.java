package es.pcomida.proyectocomida.plato.mapper;

import es.pcomida.proyectocomida.plato.dto.PlatoCreateDto;
import es.pcomida.proyectocomida.plato.dto.PlatoResponseDto;
import es.pcomida.proyectocomida.plato.dto.PlatoUpdateDto;
import es.pcomida.proyectocomida.plato.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlatoMapperTest {

    private PlatoMapper platoMapper;

    @BeforeEach
    void setUp() {
        platoMapper = new PlatoMapper();
    }

    // -------------------------------------------------------
    // toPlato(PlatoCreateDto)
    // -------------------------------------------------------

    @Test
    void toPlato_fromCreateDto_mapsAllFields() {
        PlatoCreateDto dto = PlatoCreateDto.builder()
                .nombre("Ensalada César")
                .descripcion("Lechuga, crutones y parmesano")
                .tipo(Tipo.ALMUERZO)
                .categoria(Categoria.ENTRANTE)
                .pais(Pais.ESPANOL)
                .variante(Variante.ESTANDAR)
                .precio(12.5)
                .cantidad(50)
                .isPremium(false)
                .build();

        Plato result = platoMapper.toPlato(dto);

        assertAll(
                () -> assertEquals("Ensalada César", result.getNombre()),
                () -> assertEquals("Lechuga, crutones y parmesano", result.getDescripcion()),
                () -> assertEquals(Tipo.ALMUERZO, result.getTipo()),
                () -> assertEquals(Categoria.ENTRANTE, result.getCategoria()),
                () -> assertEquals(Pais.ESPANOL, result.getPais()),
                () -> assertEquals(Variante.ESTANDAR, result.getVariante()),
                () -> assertEquals(12.5, result.getPrecio()),
                () -> assertEquals(50, result.getCantidad()),
                () -> assertFalse(result.isPremium()),
                () -> assertFalse(result.isDeleted())
        );
    }

    @Test
    void toPlato_fromCreateDto_isPremiumTrue() {
        PlatoCreateDto dto = PlatoCreateDto.builder()
                .nombre("Trufa negra")
                .descripcion("Plato premium")
                .tipo(Tipo.CENA)
                .categoria(Categoria.PRINCIPAL)
                .pais(Pais.ITALIANO)
                .variante(Variante.ESTANDAR)
                .precio(50.0)
                .cantidad(10)
                .isPremium(true)
                .build();

        Plato result = platoMapper.toPlato(dto);

        assertTrue(result.isPremium());
    }

    // -------------------------------------------------------
    // toPlato(PlatoUpdateDto, Plato)
    // -------------------------------------------------------

    @Test
    void toPlato_fromUpdateDto_updatesOnlyNonNullFields() {
        Plato existing = Plato.builder()
                .id(1L)
                .nombre("Nombre original")
                .descripcion("Descripción original")
                .tipo(Tipo.ALMUERZO)
                .categoria(Categoria.ENTRANTE)
                .pais(Pais.ESPANOL)
                .variante(Variante.ESTANDAR)
                .precio(10.0)
                .cantidad(20)
                .build();

        PlatoUpdateDto dto = PlatoUpdateDto.builder()
                .nombre("Nombre actualizado")
                .precio(15.0)
                .build();

        Plato result = platoMapper.toPlato(dto, existing);

        assertAll(
                () -> assertEquals("Nombre actualizado", result.getNombre()),
                () -> assertEquals(15.0, result.getPrecio()),
                () -> assertEquals("Descripción original", result.getDescripcion()),
                () -> assertEquals(Tipo.ALMUERZO, result.getTipo()),
                () -> assertEquals(20, result.getCantidad())
        );
    }

    @Test
    void toPlato_fromUpdateDto_allNullKeepsOriginal() {
        Plato existing = Plato.builder()
                .nombre("Original")
                .descripcion("Desc")
                .tipo(Tipo.CENA)
                .categoria(Categoria.PRINCIPAL)
                .pais(Pais.JAPONES)
                .variante(Variante.ESTANDAR)
                .precio(20.0)
                .cantidad(5)
                .build();

        PlatoUpdateDto dto = new PlatoUpdateDto();

        Plato result = platoMapper.toPlato(dto, existing);

        assertAll(
                () -> assertEquals("Original", result.getNombre()),
                () -> assertEquals("Desc", result.getDescripcion()),
                () -> assertEquals(20.0, result.getPrecio()),
                () -> assertEquals(5, result.getCantidad())
        );
    }

    @Test
    void toPlato_fromUpdateDto_isPremiumUpdated() {
        Plato existing = Plato.builder()
                .nombre("Plato")
                .descripcion("Desc")
                .tipo(Tipo.ALMUERZO)
                .categoria(Categoria.ENTRANTE)
                .pais(Pais.ESPANOL)
                .variante(Variante.ESTANDAR)
                .precio(10.0)
                .cantidad(5)
                .build();

        PlatoUpdateDto dto = PlatoUpdateDto.builder().isPremium(true).build();

        Plato result = platoMapper.toPlato(dto, existing);

        assertTrue(result.isPremium());
    }

    // -------------------------------------------------------
    // toPlatoResponseDto
    // -------------------------------------------------------

    @Test
    void toPlatoResponseDto_mapsAllFields() {
        Plato plato = Plato.builder()
                .id(1L)
                .nombre("Salmón a la plancha")
                .descripcion("Filete de salmón fresco")
                .tipo(Tipo.CENA)
                .categoria(Categoria.PRINCIPAL)
                .pais(Pais.JAPONES)
                .variante(Variante.ESTANDAR)
                .precio(18.5)
                .cantidad(30)
                .isPremium(false)
                .build();

        PlatoResponseDto result = platoMapper.toPlatoResponseDto(plato);

        assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Salmón a la plancha", result.getNombre()),
                () -> assertEquals("Filete de salmón fresco", result.getDescripcion()),
                () -> assertEquals(Tipo.CENA, result.getTipo()),
                () -> assertEquals(Categoria.PRINCIPAL, result.getCategoria()),
                () -> assertEquals(Pais.JAPONES, result.getPais()),
                () -> assertEquals(Variante.ESTANDAR, result.getVariante()),
                () -> assertEquals(18.5, result.getPrecio()),
                () -> assertEquals(30, result.getCantidad()),
                () -> assertFalse(result.isPremium())
        );
    }

    // -------------------------------------------------------
    // toResponseDtoList
    // -------------------------------------------------------

    @Test
    void toResponseDtoList_returnsCorrectSize() {
        Plato p1 = Plato.builder().id(1L).nombre("P1").descripcion("D1")
                .tipo(Tipo.ALMUERZO).categoria(Categoria.ENTRANTE).pais(Pais.ESPANOL)
                .variante(Variante.ESTANDAR).precio(10.0).cantidad(5).build();
        Plato p2 = Plato.builder().id(2L).nombre("P2").descripcion("D2")
                .tipo(Tipo.CENA).categoria(Categoria.PRINCIPAL).pais(Pais.ITALIANO)
                .variante(Variante.ESTANDAR).precio(20.0).cantidad(3).build();

        List<PlatoResponseDto> result = platoMapper.toResponseDtoList(List.of(p1, p2));

        assertEquals(2, result.size());
        assertEquals("P1", result.get(0).getNombre());
        assertEquals("P2", result.get(1).getNombre());
    }

    @Test
    void toResponseDtoList_emptyList_returnsEmpty() {
        List<PlatoResponseDto> result = platoMapper.toResponseDtoList(List.of());
        assertTrue(result.isEmpty());
    }
}