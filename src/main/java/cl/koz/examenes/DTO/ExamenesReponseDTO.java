package cl.koz.examenes.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamenesReponseDTO {

    private Integer numExamen;
    private String tipoExamen;
    private Date fechaSolicitud;
    private String resultado;

    private Integer historialNumFicha;
    private String diagnostico;
    private String pacienteRun;
}
