package com.jugador_progreso.jugador_progreso.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.jugador_progreso.jugador_progreso.Controller.V2.JugadorController;
import com.jugador_progreso.jugador_progreso.DTO.JugadorDTO;

@Component
public class JugadorModelAssembler implements RepresentationModelAssembler<JugadorDTO, EntityModel<JugadorDTO>> {

    @Override
    public EntityModel<JugadorDTO> toModel(JugadorDTO jugador) {
        return EntityModel.of(jugador, //
                linkTo(methodOn(JugadorController.class).obtenerPorId(jugador.getIdJugador())).withSelfRel(),
                linkTo(methodOn(JugadorController.class).listarTodos()).withRel("jugadores"));
    }

}
