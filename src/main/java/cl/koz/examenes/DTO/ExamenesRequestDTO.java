package cl.koz.examenes.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamenesRequestDTO{
    @NotBlank(message = "El tipo de examen no puede estar vacio!!")
    private String tipoExamen;

    @NotNull(message = "La fecha de solicitud es OBLIGATORIA")
    private Date fechaSolicitud;

    @NotBlank(message = "El resulta no puede estar vacio y es OBLIGATORIO!!!")
    private String resultado;

    @NotNull(message = "No puede estar vacio")
    private Integer historialNumFicha;

    @NotBlank(message = "El RUN del paciente es OBLIGATORIO")
    private String pacienteRun;
}
