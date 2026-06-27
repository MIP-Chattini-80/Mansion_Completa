package com.Mansion.HabitacionesMC.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.Mansion.HabitacionesMC.Controller.V2.EventosController;
import com.Mansion.HabitacionesMC.DTO.EventosDTO;

@Component
public class EventosModelAssembler implements RepresentationModelAssembler<EventosDTO, EntityModel<EventosDTO>> {

    @Override
    public EntityModel<EventosDTO> toModel(EventosDTO eventos) {
        return EntityModel.of(eventos, //
                linkTo(methodOn(EventosController.class).obtenerPorId(eventos.getIdEventos())).withSelfRel(),
                linkTo(methodOn(EventosController.class).listarCategorias()).withRel("eventos"));
    }

}