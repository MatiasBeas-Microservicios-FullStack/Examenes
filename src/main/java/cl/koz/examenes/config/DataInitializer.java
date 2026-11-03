package cl.koz.examenes.config;

import cl.koz.examenes.model.Examenes;
import cl.koz.examenes.model.ExamenesId;
import cl.koz.examenes.model.Role;
import cl.koz.examenes.model.User;
import cl.koz.examenes.repository.ExamenesRepository;
import cl.koz.examenes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ExamenesRepository examenesRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void run(String... args) throws Exception {
        // Crear usuario si no existe
        if (userRepository.findByUsername("Maty").isEmpty()) {
            User user = new User();
            user.setUsername("Maty");
            user.setPassword(passwordEncoder.encode("1234"));
            user.setRole(Role.ADMIN);
            userRepository.save(user);
            log.info(">>> DataInitializer: Usuario Maty creado correctamente.");
        }
        if (examenesRepository.count() > 0) {
            log.info(">>> DataInitializer: La BD ya tiene datos, se omite la carga inicial.");
            return;
        }
        log.info(">>> DataInitializer: BD vacía detectada, insertando datos de prueba...");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        examenesRepository.save(new Examenes(
                new ExamenesId(1, 1),
                "Hemograma completo",
                sdf.parse("2024-01-20"),
                "Valores normales",
                "22.359.190-6"
        ));
        examenesRepository.save(new Examenes(
                new ExamenesId(2, 2),
                "Glucosa en sangre",
                sdf.parse("2024-02-25"),
                "Glucosa elevada 180mg/dL",
                "18.765.432-1"
        ));
        examenesRepository.save(new Examenes(
                new ExamenesId(3, 3),
                "Radiografía de muñeca",
                sdf.parse("2024-03-12"),
                "Fractura confirmada en radio distal",
                "11.111.111-1"
        ));

        log.info(">>> DataInitializer: {} exámenes insertados correctamente.",
                examenesRepository.count());
    }
}

