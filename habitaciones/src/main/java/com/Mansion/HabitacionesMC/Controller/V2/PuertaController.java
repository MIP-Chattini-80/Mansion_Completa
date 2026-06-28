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

import com.Mansion.HabitacionesMC.Assemblers.PuertaModelAssembler;
import com.Mansion.HabitacionesMC.DTO.PuertaDTO;
import com.Mansion.HabitacionesMC.Model.Puerta;
import com.Mansion.HabitacionesMC.Service.PuertaService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("puertaControllerV2")
@RequestMapping("/api/v2/puertas")
@Validated
public class PuertaController {

    @Autowired
    private PuertaService puertaService;

    @Autowired
    private PuertaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<PuertaDTO>>> listarTodas() {
        try {
            List<EntityModel<PuertaDTO>> puertas = puertaService.listarTodas().stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            if (puertas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                    puertas,
                    linkTo(methodOn(PuertaController.class).listarTodas()).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PuertaDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            PuertaDTO dto = puertaService.obtenerPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PuertaDTO>> guardarPuerta(@Valid @RequestBody Puerta puerta) {
        try {
            PuertaDTO guardada = puertaService.guardarPuerta(puerta);
            return ResponseEntity
                    .created(linkTo(methodOn(PuertaController.class).obtenerPorId(guardada.getIdPuerta())).toUri())
                    .body(assembler.toModel(guardada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PuertaDTO>> actualizarPuerta(@PathVariable Long id,
            @Valid @RequestBody Puerta datosNuevos) {
        try {
            PuertaDTO actualizada = puertaService.actualizarPuerta(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(actualizada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PuertaDTO>> editarPuerta(@PathVariable Long id,
            @RequestBody PuertaDTO datosNuevos) {
        try {
            PuertaDTO editada = puertaService.editarPuerta(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(editada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            puertaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}