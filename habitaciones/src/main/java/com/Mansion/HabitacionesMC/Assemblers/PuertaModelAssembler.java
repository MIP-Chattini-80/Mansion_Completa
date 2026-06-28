package com.Mansion.HabitacionesMC.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.Mansion.HabitacionesMC.Controller.V2.PuertaController;
import com.Mansion.HabitacionesMC.DTO.PuertaDTO;

@Component
public class PuertaModelAssembler implements RepresentationModelAssembler<PuertaDTO, EntityModel<PuertaDTO>> {

    @Override
    public EntityModel<PuertaDTO> toModel(PuertaDTO puerta) {
        return EntityModel.of(puerta, //
                linkTo(methodOn(PuertaController.class).obtenerPorId(puerta.getIdPuerta())).withSelfRel(),
                linkTo(methodOn(PuertaController.class).listarTodas()).withRel("puertas"));
    }

}