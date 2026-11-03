package cl.koz.examenes.repository;

import cl.koz.examenes.model.Examenes;
import cl.koz.examenes.model.ExamenesId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamenesRepository extends JpaRepository<Examenes, ExamenesId> {
}
