package com.Mansion.HabitacionesMC.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.Mansion.HabitacionesMC.Controller.V2.EventoController;
import com.Mansion.HabitacionesMC.DTO.EventoDTO;

@Component
public class EventoModelAssembler implements RepresentationModelAssembler<EventoDTO, EntityModel<EventoDTO>> {

    @Override
    public EntityModel<EventoDTO> toModel(EventoDTO evento) {
        return EntityModel.of(evento,
                linkTo(methodOn(EventoController.class).buscarPorId(evento.getIdEvento())).withSelfRel(),
                linkTo(methodOn(EventoController.class).listarEventosDTO()).withRel("eventos"));
    }

}