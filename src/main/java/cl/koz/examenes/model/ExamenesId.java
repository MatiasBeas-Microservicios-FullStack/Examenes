package cl.koz.examenes.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@Embeddable
@AllArgsConstructor
public class ExamenesId implements Serializable {
    private Integer numeroExamenes;
    private Integer historialNumb;
}
