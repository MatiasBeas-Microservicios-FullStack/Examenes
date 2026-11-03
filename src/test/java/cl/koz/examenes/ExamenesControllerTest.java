package cl.koz.examenes;

import cl.koz.examenes.DTO.ExamenesReponseDTO;
import cl.koz.examenes.DTO.ExamenesRequestDTO;
import cl.koz.examenes.controller.ExamenesController;
import cl.koz.examenes.model.ExamenesId;
import cl.koz.examenes.security.JwtAuthFilter;
import cl.koz.examenes.service.ExameneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ExamenesController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExamenesControllerTest {

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private ExameneService exameneService;

    private ExamenesReponseDTO dto() {
        return new ExamenesReponseDTO(
                1,
                "Sangre",
                Date.valueOf(LocalDate.now()),
                "Normal",
                100,
                "Diabetes",
                "11-1"
        );
    }

    @Test
    void obtenerTodos() throws Exception {

        when(exameneService.obtenerTodosExamen())
                .thenReturn(List.of(dto()));

        mockMvc.perform(get("/examenes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipoExamen").value("Sangre"));
    }

    @Test
    void obtenerPorIdExiste() throws Exception {

        when(exameneService.obtenerPorId(any()))
                .thenReturn(Optional.of(dto()));

        mockMvc.perform(get("/examenes/1/100"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorIdNoExiste() throws Exception {

        when(exameneService.obtenerPorId(any()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/examenes/1/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear() throws Exception {

        ExamenesRequestDTO request = new ExamenesRequestDTO(
                "Sangre",
                Date.valueOf(LocalDate.now()),
                "Normal",
                100,
                "11-1"
        );

        when(exameneService.guardar(any()))
                .thenReturn(dto());

        mockMvc.perform(post("/examenes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void actualizarExiste() throws Exception {

        ExamenesRequestDTO request = new ExamenesRequestDTO(
                "Orina",
                Date.valueOf(LocalDate.now()),
                "Alterado",
                100,
                "11-1"
        );

        when(exameneService.actualizar(any(), any()))
                .thenReturn(Optional.of(dto()));

        mockMvc.perform(put("/examenes/1/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void actualizarNoExiste() throws Exception {

        ExamenesRequestDTO request = new ExamenesRequestDTO(
                "Orina",
                Date.valueOf(LocalDate.now()),
                "Alterado",
                100,
                "11-1"
        );

        when(exameneService.actualizar(any(), any()))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/examenes/1/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarExiste() throws Exception {

        when(exameneService.obtenerPorId(any()))
                .thenReturn(Optional.of(dto()));

        mockMvc.perform(delete("/examenes/1/100"))
                .andExpect(status().isNoContent());

        verify(exameneService).eliminar(any());
    }

    @Test
    void eliminarNoExiste() throws Exception {

        when(exameneService.obtenerPorId(any()))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/examenes/1/100"))
                .andExpect(status().isNotFound());
    }

}