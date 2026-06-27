package com.jugador_progreso.jugador_progreso.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.jugador_progreso.jugador_progreso.Controller.V2.ProgresoController;
import com.jugador_progreso.jugador_progreso.DTO.ProgresoDTO;

@Component
public class ProgresoModelAssembler implements RepresentationModelAssembler<ProgresoDTO, EntityModel<ProgresoDTO>> {

    @Override
    public EntityModel<ProgresoDTO> toModel(ProgresoDTO progreso) {
        return EntityModel.of(progreso,
                linkTo(methodOn(ProgresoController.class).obtenerPorId(progreso.getIdProgreso())).withSelfRel(),
                linkTo(methodOn(ProgresoController.class).listarTodos()).withRel("progresos"));
    }

}