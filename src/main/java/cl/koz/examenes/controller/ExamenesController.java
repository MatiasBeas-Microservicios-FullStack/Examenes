package cl.koz.examenes.controller;

import cl.koz.examenes.DTO.ExamenesReponseDTO;
import cl.koz.examenes.DTO.ExamenesRequestDTO;
import cl.koz.examenes.model.ExamenesId;
import cl.koz.examenes.service.ExameneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/examenes")
@Tag(name = "Examenes", description = "Operaciones relacionadas con los Examenes")
@RequiredArgsConstructor
public class ExamenesController {
    private final ExameneService exameneService;

    @GetMapping
    @Operation(summary = "Obtener todos los examanes", description = "Entrega una lista de todos los examenes registrados")
    public ResponseEntity<List<ExamenesReponseDTO>> obtenerTodos(){
        return ResponseEntity.ok(exameneService.obtenerTodosExamen());
    }

    @GetMapping("/{numeroExamen}/{historialNumb}")
    @Operation(summary = "Obtener examenes por ID's", description = "Retorna el examen buscado por ID'S")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Examen encontrado"),
            @ApiResponse(responseCode = "404", description = "Examen ingresado no encontrado!")
    })
    public ResponseEntity<ExamenesReponseDTO> obtenerPorId(@PathVariable Integer numeroExamen, @PathVariable Integer historialNumb) {
        ExamenesId id = new ExamenesId(numeroExamen, historialNumb);
        return exameneService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo Examen",description = "Guarda el nuevo examen ingresado")
    @ApiResponse(responseCode = "201", description = "Examen creado exitosamente")
    public ResponseEntity<ExamenesReponseDTO> crearExamen(@Valid @RequestBody ExamenesRequestDTO dto){
        ExamenesReponseDTO reponseDTO = exameneService.guardar(dto);
        return ResponseEntity.status(201).body(reponseDTO);
    }

    @PutMapping("/{numeroExamen}/{historialNumb}")
    @Operation(summary = "Actualizar examen existente",description = "Actualiza y guarda la informacion cambiada en el examen buscado por ID's")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Examen actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "El examen ingresado no existe")
    })
    public ResponseEntity<ExamenesReponseDTO> actualizar(@PathVariable Integer numeroExamen, @PathVariable Integer historialNumb, @Valid @RequestBody ExamenesRequestDTO dto) {
        ExamenesId id = new ExamenesId(numeroExamen, historialNumb);
        return exameneService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{numeroExamen}/{historialNumb}")
    @Operation(summary ="Eliminar examen existente", description = "Elimina un examen buscado por ID's")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Examen eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "El examen ingresado no existe")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Integer numeroExamen, @PathVariable Integer historialNumb) {
        ExamenesId id = new ExamenesId(numeroExamen, historialNumb);
        if (exameneService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        exameneService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

