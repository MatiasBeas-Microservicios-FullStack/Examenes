package cl.koz.examenes.assemblers;

import cl.koz.examenes.DTO.ExamenesReponseDTO;
import cl.koz.examenes.controller.ExamenesController;
import cl.koz.examenes.model.Examenes;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ExamenesModelAssembler implements RepresentationModelAssembler<ExamenesReponseDTO, EntityModel<ExamenesReponseDTO>> {
    @Override
    public EntityModel<ExamenesReponseDTO> toModel(ExamenesReponseDTO examenes){
        return EntityModel.of(examenes,
                linkTo(methodOn(ExamenesController.class).obtenerPorId(examenes.getNumExamen(),examenes.getHistorialNumFicha())).withSelfRel(),
                linkTo(methodOn(ExamenesController.class).obtenerTodos()).withRel("todos-los-examanes")
                );
    }
}
