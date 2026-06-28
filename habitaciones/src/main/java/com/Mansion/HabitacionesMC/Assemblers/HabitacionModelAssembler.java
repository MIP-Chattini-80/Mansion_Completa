package com.Mansion.HabitacionesMC.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.Mansion.HabitacionesMC.Controller.V2.HabitacionController;
import com.Mansion.HabitacionesMC.DTO.HabitacionDTO;

@Component
public class HabitacionModelAssembler
        implements RepresentationModelAssembler<HabitacionDTO, EntityModel<HabitacionDTO>> {

    @Override
    public EntityModel<HabitacionDTO> toModel(HabitacionDTO habi) {
        return EntityModel.of(habi,
                linkTo(methodOn(HabitacionController.class).obtenerPorId(habi.getIdHabitacion())).withSelfRel(),
                linkTo(methodOn(HabitacionController.class).listarTodas()).withRel("habitaciones"));
    }

}
