package cl.koz.examenes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Examenes")

public class Examenes {
    @EmbeddedId
    private ExamenesId id;

    @Column(nullable = false, length = 100)
    private String tipoExamen;

    @Column(nullable = false)
    private Date fechaSolicitud;

    @Column(nullable = false, length = 200)
    private String resultado;

    @Column(nullable = false, length = 20)
    private String pacienteRun;
}
