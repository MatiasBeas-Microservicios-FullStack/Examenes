package cl.koz.examenes.webClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HistorialClient {
    private final WebClient webClient;

    public String obtenerDiagnostico(Integer historialNumFicha, String pacienteRun){
        try {
            Map response = webClient
                    .get()
                    .uri("/historialMedico/" + historialNumFicha + "/" + pacienteRun)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response != null ? (String) response.get("diagnostico") : "Sin Historial";
        } catch (Exception e) {
            return "Sin Historial";
        }
    }
    public boolean existeHistorial(Integer historialNumFicha) {
        try {
            Map response = webClient
                    .get()
                    .uri("/historialMedico/" + historialNumFicha) // solo por número de ficha
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response != null; // llegó algo → existe
        } catch (Exception e) {
            return false; // no existe o servicio caído
        }
}
}
