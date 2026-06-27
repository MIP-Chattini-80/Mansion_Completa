package com.mansion_2.personajes.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mansion_2.personajes.controller.V2.PersonajeController;
import com.mansion_2.personajes.dto.PersonajeDTO;

@Component
public class PersonajeModelAssembler implements RepresentationModelAssembler<PersonajeDTO, EntityModel<PersonajeDTO>> {

    @Override
    public EntityModel<PersonajeDTO> toModel(PersonajeDTO personaje) {
        return EntityModel.of(personaje,
                linkTo(methodOn(PersonajeController.class).obtenerPorId(personaje.getId())).withSelfRel(),
                linkTo(methodOn(PersonajeController.class).listarTodo()).withRel("personajes"));
    }

}