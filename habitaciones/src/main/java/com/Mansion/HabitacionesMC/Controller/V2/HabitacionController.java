package com.Mansion.HabitacionesMC.Controller.V2;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mansion.HabitacionesMC.Assemblers.HabitacionModelAssembler;
import com.Mansion.HabitacionesMC.DTO.HabitacionDTO;
import com.Mansion.HabitacionesMC.Model.Habitacion;
import com.Mansion.HabitacionesMC.Service.HabitacionService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("habitacionControllerV2")
@RequestMapping("/api/v2/habitaciones")
@Validated
public class HabitacionController {

    @Autowired
    private HabitacionService habitacionService;

    @Autowired
    private HabitacionModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<HabitacionDTO>>> listarTodas() {
        List<EntityModel<HabitacionDTO>> habitaciones = habitacionService.listarTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (habitaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                habitaciones,
                linkTo(methodOn(HabitacionController.class).listarTodas()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<HabitacionDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            HabitacionDTO dto = habitacionService.obtenerPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/nombre/{nombre}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<HabitacionDTO>> buscarPorNombre(@PathVariable String nombre) {
        try {
            HabitacionDTO dto = habitacionService.buscarPorNombre(nombre);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<HabitacionDTO>> guardarHabitacion(@Valid @RequestBody Habitacion habitacion) {
        try {
            HabitacionDTO guardada = habitacionService.guardarHabitacion(habitacion);
            return ResponseEntity
                    .created(linkTo(methodOn(HabitacionController.class).obtenerPorId(guardada.getIdHabitacion()))
                            .toUri())
                    .body(assembler.toModel(guardada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<HabitacionDTO>> actualizarHabitacion(@PathVariable Long id,
            @Valid @RequestBody HabitacionDTO datosNuevos) {
        try {
            HabitacionDTO actualizada = habitacionService.actualizarHabitacion(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(actualizada));
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("La habitación no existe")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<HabitacionDTO>> editarHabitacion(@PathVariable Long id,
            @RequestBody HabitacionDTO datosNuevos) {
        try {
            HabitacionDTO editada = habitacionService.editarHabitacion(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(editada));
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("La habitación no existe")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            habitacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
