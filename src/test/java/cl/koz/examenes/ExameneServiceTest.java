package cl.koz.examenes;

import cl.koz.examenes.DTO.ExamenesReponseDTO;
import cl.koz.examenes.DTO.ExamenesRequestDTO;
import cl.koz.examenes.model.Examenes;
import cl.koz.examenes.model.ExamenesId;
import cl.koz.examenes.repository.ExamenesRepository;
import cl.koz.examenes.security.JwtAuthFilter;
import cl.koz.examenes.service.ExameneService;
import cl.koz.examenes.webClient.HistorialClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExameneServiceTest {

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private ExamenesRepository repository;

    @Mock
    private HistorialClient historialClient;

    @InjectMocks
    private ExameneService service;

    private Examenes examen() {
        return new Examenes(
                new ExamenesId(1,100),
                "Sangre",
                Date.valueOf(LocalDate.now()),
                "Normal",
                "11-1"
        );
    }

    @Test
    void obtenerTodos() {

        when(repository.findAll()).thenReturn(List.of(examen()));
        when(historialClient.obtenerDiagnostico(100,"11-1"))
                .thenReturn("Diabetes");

        List<ExamenesReponseDTO> lista = service.obtenerTodosExamen();

        assertEquals(1, lista.size());
        verify(repository).findAll();
    }

    @Test
    void obtenerPorIdExiste() {

        Examenes examen = examen();

        when(repository.findById(examen.getId()))
                .thenReturn(Optional.of(examen));

        when(historialClient.obtenerDiagnostico(100,"11-1"))
                .thenReturn("Diabetes");

        Optional<ExamenesReponseDTO> dto =
                service.obtenerPorId(examen.getId());

        assertTrue(dto.isPresent());
        assertEquals("Sangre", dto.get().getTipoExamen());
    }

    @Test
    void obtenerPorIdNoExiste() {

        ExamenesId id = new ExamenesId(1,100);

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        Optional<ExamenesReponseDTO> dto = service.obtenerPorId(id);

        assertTrue(dto.isEmpty());
    }

    @Test
    void guardarCorrectamente() {

        ExamenesRequestDTO dto = new ExamenesRequestDTO(
                "Sangre",
                Date.valueOf(LocalDate.now()),
                "Normal",
                100,
                "11-1"
        );

        when(historialClient.existeHistorial(100))
                .thenReturn(true);

        when(repository.count()).thenReturn(0L);

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(historialClient.obtenerDiagnostico(100,"11-1"))
                .thenReturn("Diabetes");

        ExamenesReponseDTO respuesta = service.guardar(dto);

        assertNotNull(respuesta);
        verify(repository).save(any());
    }

    @Test
    void guardarHistorialNoExiste() {

        ExamenesRequestDTO dto = new ExamenesRequestDTO(
                "Sangre",
                Date.valueOf(LocalDate.now()),
                "Normal",
                100,
                "11-1"
        );

        when(historialClient.existeHistorial(100))
                .thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> service.guardar(dto));

        verify(repository, never()).save(any());
    }

    @Test
    void actualizarExiste() {

        Examenes examen = examen();

        ExamenesRequestDTO dto = new ExamenesRequestDTO(
                "Orina",
                Date.valueOf(LocalDate.now()),
                "Alterado",
                100,
                "11-1"
        );

        when(repository.findById(examen.getId()))
                .thenReturn(Optional.of(examen));

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(historialClient.obtenerDiagnostico(100,"11-1"))
                .thenReturn("Diabetes");

        Optional<ExamenesReponseDTO> actualizado =
                service.actualizar(examen.getId(), dto);

        assertTrue(actualizado.isPresent());
        assertEquals("Orina",
                actualizado.get().getTipoExamen());
    }

    @Test
    void actualizarNoExiste() {

        ExamenesId id = new ExamenesId(1,100);

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        Optional<ExamenesReponseDTO> dto =
                service.actualizar(id, mock(ExamenesRequestDTO.class));

        assertTrue(dto.isEmpty());
    }

    @Test
    void eliminar() {

        ExamenesId id = new ExamenesId(1,100);

        service.eliminar(id);

        verify(repository).deleteById(id);
    }

}