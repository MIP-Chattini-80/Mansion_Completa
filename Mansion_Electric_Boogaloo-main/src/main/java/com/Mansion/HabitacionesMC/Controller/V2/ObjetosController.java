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

import com.Mansion.HabitacionesMC.Assemblers.ObjetosModelAssembler;
import com.Mansion.HabitacionesMC.DTO.ObjetosDTO;
import com.Mansion.HabitacionesMC.Model.Objetos;
import com.Mansion.HabitacionesMC.Service.ObjetosService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("objetosControllerV2")
@RequestMapping("/api/v2/objetos")
@Validated
public class ObjetosController {

    @Autowired
    private ObjetosService objetosService;

    @Autowired
    private ObjetosModelAssembler assembler;

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ObjetosDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            ObjetosDTO dto = objetosService.obtenerPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/habitacion/{idHabitacion}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ObjetosDTO>>> buscarPorHabitacion(
            @PathVariable Long idHabitacion) {
        try {
            List<EntityModel<ObjetosDTO>> lista = objetosService.buscarPorHabitacion(idHabitacion).stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());
            if (lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                    lista,
                    linkTo(methodOn(ObjetosController.class).buscarPorHabitacion(idHabitacion)).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ObjetosDTO>> guardar(@Valid @RequestBody Objetos objetos) {
        try {
            ObjetosDTO guardado = objetosService.guardar(objetos);
            return ResponseEntity
                    .created(linkTo(methodOn(ObjetosController.class).obtenerPorId(guardado.getIdInstancia())).toUri())
                    .body(assembler.toModel(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ObjetosDTO>> actualizar(@PathVariable Long id,
            @Valid @RequestBody ObjetosDTO datosNuevos) {
        try {
            ObjetosDTO actualizado = objetosService.actualizar(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ObjetosDTO>> editar(@PathVariable Long id, @RequestBody ObjetosDTO datosNuevos) {
        try {
            ObjetosDTO editado = objetosService.editar(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(editado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            objetosService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}