package es.pcomida.proyectocomida.plato.services;

import es.pcomida.proyectocomida.plato.dto.PlatoCreateDto;
import es.pcomida.proyectocomida.plato.dto.PlatoResponseDto;
import es.pcomida.proyectocomida.plato.dto.PlatoUpdateDto;
import es.pcomida.proyectocomida.plato.exceptions.PlatoNotFoundException;
import es.pcomida.proyectocomida.plato.mapper.PlatoMapper;
import es.pcomida.proyectocomida.plato.models.Plato;
import es.pcomida.proyectocomida.plato.models.Tipo;
import es.pcomida.proyectocomida.plato.repositories.PlatosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatoServiceImplTest {

    @Mock
    private PlatosRepository platosRepository;

    @Mock
    private PlatoMapper platoMapper;

    @InjectMocks
    private PlatosServiceImpl platosService;

    @Test
    void findAll_noFilters() {
        // Arrange
        List<Plato> platos = List.of(new Plato());
        Page<Plato> page = new PageImpl<>(platos);
        PlatoResponseDto platoResponseDto = new PlatoResponseDto();

        when(platosRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(platoMapper.toPlatoResponseDto(any(Plato.class))).thenReturn(platoResponseDto);

        // Act
        Page<PlatoResponseDto> result = platosService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Pageable.unpaged(), null);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(platoResponseDto, result.getContent().get(0))
        );
    }

    @Test
    void findAll_withFilters() {
        // Arrange
        List<Plato> platos = List.of(new Plato());
        Page<Plato> page = new PageImpl<>(platos);
        PlatoResponseDto platoResponseDto = new PlatoResponseDto();

        when(platosRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(platoMapper.toPlatoResponseDto(any(Plato.class))).thenReturn(platoResponseDto);

        // Act
        Page<PlatoResponseDto> result = platosService.findAll(Optional.of("test"), Optional.of(Tipo.ALMUERZO), Optional.of(false), Pageable.unpaged(), null);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(platoResponseDto, result.getContent().get(0))
        );
    }

    @Test
    void findById_exists() {
        // Arrange
        Plato plato = new Plato();
        PlatoResponseDto platoResponseDto = new PlatoResponseDto();
        when(platosRepository.findById(1L)).thenReturn(Optional.of(plato));
        when(platoMapper.toPlatoResponseDto(plato)).thenReturn(platoResponseDto);

        // Act
        PlatoResponseDto result = platosService.findById(1L);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(platoResponseDto, result)
        );
    }

    @Test
    void findById_notExists() {
        // Arrange
        when(platosRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlatoNotFoundException.class, () -> platosService.findById(1L));
    }

    @Test
    void save() {
        // Arrange
        PlatoCreateDto platoCreateDto = new PlatoCreateDto();
        Plato plato = new Plato();
        PlatoResponseDto platoResponseDto = new PlatoResponseDto();

        when(platoMapper.toPlato(platoCreateDto)).thenReturn(plato);
        when(platosRepository.save(plato)).thenReturn(plato);
        when(platoMapper.toPlatoResponseDto(plato)).thenReturn(platoResponseDto);

        // Act
        PlatoResponseDto result = platosService.save(platoCreateDto);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(platoResponseDto, result)
        );
    }

    @Test
    void update_exists() {
        // Arrange
        PlatoUpdateDto platoUpdateDto = new PlatoUpdateDto();
        Plato plato = new Plato();
        PlatoResponseDto platoResponseDto = new PlatoResponseDto();

        when(platosRepository.findById(1L)).thenReturn(Optional.of(plato));
        when(platoMapper.toPlato(platoUpdateDto, plato)).thenReturn(plato);
        when(platosRepository.save(plato)).thenReturn(plato);
        when(platoMapper.toPlatoResponseDto(plato)).thenReturn(platoResponseDto);

        // Act
        PlatoResponseDto result = platosService.update(1L, platoUpdateDto);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(platoResponseDto, result)
        );
    }

    @Test
    void update_notExists() {
        // Arrange
        PlatoUpdateDto platoUpdateDto = new PlatoUpdateDto();
        when(platosRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlatoNotFoundException.class, () -> platosService.update(1L, platoUpdateDto));
    }

    @Test
    void deleteById_exists() {
        // Arrange
        Plato plato = new Plato();
        when(platosRepository.findById(1L)).thenReturn(Optional.of(plato));

        // Act
        platosService.deleteById(1L);

        // Assert
        verify(platosRepository).delete(plato);
    }

    @Test
    void deleteById_notExists() {
        // Arrange
        when(platosRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlatoNotFoundException.class, () -> platosService.deleteById(1L));
    }
}
