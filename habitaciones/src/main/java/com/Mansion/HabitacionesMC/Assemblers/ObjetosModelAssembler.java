package com.Mansion.HabitacionesMC.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.Mansion.HabitacionesMC.Controller.V2.ObjetosController;
import com.Mansion.HabitacionesMC.DTO.ObjetosDTO;

@Component
public class ObjetosModelAssembler implements RepresentationModelAssembler<ObjetosDTO, EntityModel<ObjetosDTO>> {

    @Override
    public EntityModel<ObjetosDTO> toModel(ObjetosDTO objeto) {
        return EntityModel.of(objeto, //
                linkTo(methodOn(ObjetosController.class).obtenerPorId(objeto.getIdInstancia())).withSelfRel(),
                linkTo(methodOn(ObjetosController.class).listarTodo()).withRel("objetos"));
    }

}
