package com.Mansion.HabitacionesMC.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.Mansion.HabitacionesMC.Controller.V2.ObjetoController;
import com.Mansion.HabitacionesMC.DTO.ObjetoDTO;

@Component
public class ObjetoModelAssembler implements RepresentationModelAssembler<ObjetoDTO, EntityModel<ObjetoDTO>> {

    @Override
    public EntityModel<ObjetoDTO> toModel(ObjetoDTO objeto) {
        return EntityModel.of(objeto, //
                linkTo(methodOn(ObjetoController.class).obtenerPorId(objeto.getIdObjeto())).withSelfRel(),
                linkTo(methodOn(ObjetoController.class).listarTodo()).withRel("objetos"));
    }
}
