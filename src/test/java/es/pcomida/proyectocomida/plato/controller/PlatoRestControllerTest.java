package es.pcomida.proyectocomida.plato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.pcomida.proyectocomida.plato.dto.PlatoCreateDto;
import es.pcomida.proyectocomida.plato.dto.PlatoResponseDto;
import es.pcomida.proyectocomida.plato.dto.PlatoUpdateDto;
import es.pcomida.proyectocomida.plato.exceptions.PlatoBadRequestException;
import es.pcomida.proyectocomida.plato.exceptions.PlatoNotFoundException;
import es.pcomida.proyectocomida.plato.models.Categoria;
import es.pcomida.proyectocomida.plato.models.Pais;
import es.pcomida.proyectocomida.plato.models.Tipo;
import es.pcomida.proyectocomida.plato.services.PlatosService;
import es.pcomida.proyectocomida.usuario.models.Usuario;
import es.pcomida.proyectocomida.utils.pagination.PaginationLinksUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("PlatoRestController — Tests de capa web")
class PlatoRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PlatosService platosService;

    @Mock
    private PaginationLinksUtils paginationLinksUtils;

    private PlatoResponseDto responseDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new PlatoRestController(platosService, paginationLinksUtils))
                .addPlaceholderValue("api.version", "v1")
                .build();
        objectMapper = new ObjectMapper();

        responseDto = PlatoResponseDto.builder()
                .id(1L)
                .nombre("Paella Valenciana")
                .descripcion("Paella tradicional de Valencia")
                .tipo(Tipo.ALMUERZO)
                .categoria(Categoria.PRINCIPAL)
                .pais(Pais.ESPANOL)
                .precio(15.50)
                .cantidad(10)
                .isPremium(false)
                .build();
    }

    // ── GET /v1/platos ────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("GET /v1/platos")
    class GetAll {

        @Test
        @DisplayName("Obtiene la lista paginada de platos")
        void getAll_sinFiltros_devuelve200() throws Exception {
            Page<PlatoResponseDto> page = new PageImpl<>(List.of(responseDto));

            when(platosService.findAll(any(), any(), any(), any(Pageable.class), any(Usuario.class)))
                    .thenReturn(page);
            when(paginationLinksUtils.createLinkHeader(any(), any())).thenReturn("");

            mockMvc.perform(get("/v1/platos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].nombre").value("Paella Valenciana"));
        }

        @Test
        @DisplayName("Filtra platos por nombre")
        void getAll_conNombre_devuelve200() throws Exception {
            Page<PlatoResponseDto> page = new PageImpl<>(List.of(responseDto));

            when(platosService.findAll(any(), any(), any(), any(Pageable.class), any(Usuario.class)))
                    .thenReturn(page);
            when(paginationLinksUtils.createLinkHeader(any(), any())).thenReturn("");

            mockMvc.perform(get("/v1/platos?nombre=Paella"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].nombre").value("Paella Valenciana"));
        }

        @Test
        @DisplayName("Filtra platos por tipo")
        void getAll_conTipo_devuelve200() throws Exception {
            Page<PlatoResponseDto> page = new PageImpl<>(List.of(responseDto));

            when(platosService.findAll(any(), any(), any(), any(Pageable.class), any(Usuario.class)))
                    .thenReturn(page);
            when(paginationLinksUtils.createLinkHeader(any(), any())).thenReturn("");

            mockMvc.perform(get("/v1/platos?tipo=ALMUERZO"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Retorna lista vacía cuando no hay resultados")
        void getAll_sinResultados_devuelve200Vacio() throws Exception {
            Page<PlatoResponseDto> emptyPage = new PageImpl<>(List.of());

            when(platosService.findAll(any(), any(), any(), any(Pageable.class), any(Usuario.class)))
                    .thenReturn(emptyPage);
            when(paginationLinksUtils.createLinkHeader(any(), any())).thenReturn("");

            mockMvc.perform(get("/v1/platos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content.length()").value(0));
        }
    }

    // ── GET /v1/platos/{id} ───────────────────────────────────────────────────────

    @Nested
    @DisplayName("GET /v1/platos/{id}")
    class GetById {

        @Test
        @DisplayName("Obtiene plato por id existente")
        void getById_idExiste_devuelve200() throws Exception {
            when(platosService.findById(1L)).thenReturn(responseDto);

            mockMvc.perform(get("/v1/platos/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.nombre").value("Paella Valenciana"))
                    .andExpect(jsonPath("$.precio").value(15.50));
        }

        @Test
        @DisplayName("Recibe 404 si el plato no existe")
        void getById_idNoExiste_devuelve404() throws Exception {
            when(platosService.findById(99L)).thenThrow(new PlatoNotFoundException(99L));

            mockMvc.perform(get("/v1/platos/99"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Retorna los detalles completos del plato")
        void getById_idExiste_detallesCompletos() throws Exception {
            when(platosService.findById(1L)).thenReturn(responseDto);

            mockMvc.perform(get("/v1/platos/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.tipo").value("ALMUERZO"))
                    .andExpect(jsonPath("$.categoria").value("PRINCIPAL"))
                    .andExpect(jsonPath("$.cantidad").value(10));
        }
    }

    // ── POST /v1/platos ───────────────────────────────────────────────────────────

    @Nested
    @DisplayName("POST /v1/platos")
    class Create {

        @Test
        @DisplayName("ADMIN crea un nuevo plato correctamente")
        void save_datosValidos_devuelve201() throws Exception {
            PlatoCreateDto createDto = PlatoCreateDto.builder()
                    .nombre("Paella Valenciana")
                    .descripcion("Paella tradicional")
                    .tipo(Tipo.ALMUERZO)
                    .categoria(Categoria.PRINCIPAL)
                    .pais(Pais.ESPANOL)
                    .precio(15.50)
                    .cantidad(10)
                    .build();

            when(platosService.save(any(PlatoCreateDto.class))).thenReturn(responseDto);

            mockMvc.perform(post("/v1/platos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.nombre").value("Paella Valenciana"));
        }

        @Test
        @DisplayName("Recibe 400 si faltan campos obligatorios")
        void save_sinNombre_devuelve400() throws Exception {
            PlatoCreateDto createDto = PlatoCreateDto.builder()
                    .descripcion("Sin nombre")
                    .tipo(Tipo.ALMUERZO)
                    .precio(15.50)
                    .build();

            mockMvc.perform(post("/v1/platos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Recibe 400 si el nombre ya existe")
        void save_nombreDuplicado_devuelve400() throws Exception {
            PlatoCreateDto createDto = PlatoCreateDto.builder()
                    .nombre("Paella Valenciana")
                    .tipo(Tipo.ALMUERZO)
                    .precio(15.50)
                    .build();

            when(platosService.save(any(PlatoCreateDto.class)))
                    .thenThrow(new PlatoBadRequestException("El nombre ya existe"));

            mockMvc.perform(post("/v1/platos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ── PUT /v1/platos/{id} ───────────────────────────────────────────────────────

    @Nested
    @DisplayName("PUT /v1/platos/{id}")
    class Update {

        @Test
        @DisplayName("ADMIN actualiza plato correctamente")
        void update_datosValidos_devuelve200() throws Exception {
            PlatoUpdateDto updateDto = PlatoUpdateDto.builder()
                    .nombre("Paella Actualizada")
                    .precio(16.50)
                    .build();

            PlatoResponseDto updatedDto = PlatoResponseDto.builder()
                    .id(1L)
                    .nombre("Paella Actualizada")
                    .descripcion("Paella tradicional de Valencia")
                    .tipo(Tipo.ALMUERZO)
                    .categoria(Categoria.PRINCIPAL)
                    .pais(Pais.ESPANOL)
                    .precio(16.50)
                    .cantidad(10)
                    .isPremium(false)
                    .build();

            when(platosService.update(eq(1L), any(PlatoUpdateDto.class))).thenReturn(updatedDto);

            mockMvc.perform(put("/v1/platos/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Paella Actualizada"))
                    .andExpect(jsonPath("$.precio").value(16.50));
        }

        @Test
        @DisplayName("Recibe 404 si el plato no existe")
        void update_idNoExiste_devuelve404() throws Exception {
            PlatoUpdateDto updateDto = PlatoUpdateDto.builder()
                    .nombre("Nuevo nombre")
                    .build();

            when(platosService.update(eq(99L), any(PlatoUpdateDto.class)))
                    .thenThrow(new PlatoNotFoundException(99L));

            mockMvc.perform(put("/v1/platos/99")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateDto)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Recibe 400 si intenta usar nombre duplicado")
        void update_nombreDuplicado_devuelve400() throws Exception {
            PlatoUpdateDto updateDto = PlatoUpdateDto.builder()
                    .nombre("Otro Plato Existente")
                    .build();

            when(platosService.update(eq(1L), any(PlatoUpdateDto.class)))
                    .thenThrow(new PlatoBadRequestException("El nombre ya existe"));

            mockMvc.perform(put("/v1/platos/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Actualiza solo algunos campos")
        void update_camposParciales_devuelve200() throws Exception {
            PlatoUpdateDto updateDto = PlatoUpdateDto.builder()
                    .cantidad(20)
                    .build();

            PlatoResponseDto updatedDto = PlatoResponseDto.builder()
                    .id(1L)
                    .nombre("Paella Valenciana")
                    .descripcion("Paella tradicional de Valencia")
                    .tipo(Tipo.ALMUERZO)
                    .categoria(Categoria.PRINCIPAL)
                    .pais(Pais.ESPANOL)
                    .precio(15.50)
                    .cantidad(20)
                    .isPremium(false)
                    .build();

            when(platosService.update(eq(1L), any(PlatoUpdateDto.class))).thenReturn(updatedDto);

            mockMvc.perform(put("/v1/platos/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.cantidad").value(20));
        }
    }

    // ── DELETE /v1/platos/{id} ────────────────────────────────────────────────────

    @Nested
    @DisplayName("DELETE /v1/platos/{id}")
    class Delete {

        @Test
        @DisplayName("ADMIN elimina plato correctamente y recibe 204")
        void delete_idExiste_devuelve204() throws Exception {
            doNothing().when(platosService).deleteById(1L);

            mockMvc.perform(delete("/v1/platos/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Recibe 404 al eliminar un plato inexistente")
        void delete_idNoExiste_devuelve404() throws Exception {
            doThrow(new PlatoNotFoundException(99L)).when(platosService).deleteById(99L);

            mockMvc.perform(delete("/v1/platos/99"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Verifica que se llama a deleteById del servicio")
        void delete_verificarLlamada() throws Exception {
            doNothing().when(platosService).deleteById(1L);

            mockMvc.perform(delete("/v1/platos/1"));

            verify(platosService, times(1)).deleteById(1L);
        }
    }
}
