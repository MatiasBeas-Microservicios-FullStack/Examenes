package cl.koz.examenes.service;

import cl.koz.examenes.DTO.ExamenesReponseDTO;
import cl.koz.examenes.DTO.ExamenesRequestDTO;
import cl.koz.examenes.model.Examenes;
import cl.koz.examenes.model.ExamenesId;
import cl.koz.examenes.repository.ExamenesRepository;
import cl.koz.examenes.webClient.HistorialClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExameneService {

    private final ExamenesRepository examenesRepository;
    private final HistorialClient historialClient;

    private ExamenesReponseDTO mapToDTO(Examenes examenes) {
        String diagnostico = historialClient.obtenerDiagnostico(examenes.getId().getHistorialNumb(), examenes.getPacienteRun());
        return new ExamenesReponseDTO(
                examenes.getId().getNumeroExamenes(),
                examenes.getTipoExamen(),
                examenes.getFechaSolicitud(),
                examenes.getResultado(),
                examenes.getId().getHistorialNumb(),
                diagnostico,
                examenes.getPacienteRun()
        );
    }


    public List<ExamenesReponseDTO> obtenerTodosExamen() {
        return examenesRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ExamenesReponseDTO> obtenerPorId(ExamenesId id) {
        return examenesRepository.findById(id).map(this::mapToDTO);
    }

    public ExamenesReponseDTO guardar(ExamenesRequestDTO dto) {
        if (!historialClient.existeHistorial(dto.getHistorialNumFicha())) {
            throw new RuntimeException("El historial con número " + dto.getHistorialNumFicha() + " no existe");
        }

        long nextId = examenesRepository.count() + 1;
        ExamenesId id = new ExamenesId((int) nextId, dto.getHistorialNumFicha());
        Examenes examenes = new Examenes(
                id,
                dto.getTipoExamen(),
                dto.getFechaSolicitud(),
                dto.getResultado(),
                dto.getPacienteRun()
        );
        return mapToDTO(examenesRepository.save(examenes));
    }

    public Optional<ExamenesReponseDTO> actualizar(ExamenesId id, ExamenesRequestDTO dto) {
        return examenesRepository.findById(id).map(existe -> {

            existe.setTipoExamen(dto.getTipoExamen());
            existe.setFechaSolicitud(dto.getFechaSolicitud());
            existe.setResultado(dto.getResultado());

            return mapToDTO(examenesRepository.save(existe));
        });
    }

    public void eliminar(ExamenesId id) {
        examenesRepository.deleteById(id);
    }
}