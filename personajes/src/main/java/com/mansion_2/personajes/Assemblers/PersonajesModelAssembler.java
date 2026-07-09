package com.mansion_2.personajes.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mansion_2.personajes.controller.V2.PersonajesController;
import com.mansion_2.personajes.dto.PersonajesDTO;

@Component
public class PersonajesModelAssembler
        implements RepresentationModelAssembler<PersonajesDTO, EntityModel<PersonajesDTO>> {

    @Override
    public EntityModel<PersonajesDTO> toModel(PersonajesDTO categoria) {
        return EntityModel.of(categoria,
                linkTo(methodOn(PersonajesController.class).obtenerPorId(categoria.getId())).withSelfRel(),
                linkTo(methodOn(PersonajesController.class).listarTodo()).withRel("categorias"));
    }

}