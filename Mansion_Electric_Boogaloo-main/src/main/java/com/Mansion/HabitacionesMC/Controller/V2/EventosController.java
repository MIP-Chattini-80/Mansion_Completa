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

import com.Mansion.HabitacionesMC.Assemblers.EventosModelAssembler;
import com.Mansion.HabitacionesMC.DTO.EventosDTO;
import com.Mansion.HabitacionesMC.Model.Eventos;
import com.Mansion.HabitacionesMC.Service.EventosService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("eventosControllerV2")
@RequestMapping("/api/v2/categorias-eventos")
@Validated
public class EventosController {

    @Autowired
    private EventosService eventosService;

    @Autowired
    private EventosModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<EventosDTO>>> listarCategorias() {
        try {
            List<EntityModel<EventosDTO>> categorias = eventosService.listarCategorias().stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            if (categorias.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                    categorias,
                    linkTo(methodOn(EventosController.class).listarCategorias()).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EventosDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            EventosDTO categoria = eventosService.obtenerPorId(id);
            if (categoria == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(categoria));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EventosDTO>> guardarInstancia(@Valid @RequestBody Eventos eventos) {
        try {
            EventosDTO guardado = eventosService.guardarInstancia(eventos);
            return ResponseEntity
                    .created(linkTo(methodOn(EventosController.class).obtenerPorId(guardado.getIdEventos())).toUri())
                    .body(assembler.toModel(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EventosDTO>> actualizarInstancia(@PathVariable Long id,
            @Valid @RequestBody EventosDTO datosNuevos) {
        try {
            EventosDTO actualizado = eventosService.actualizarInstancia(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("No se puede actualizar")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EventosDTO>> editar(@PathVariable Long id, @RequestBody EventosDTO datosNuevos) {
        try {
            EventosDTO editado = eventosService.editar(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(editado));
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("No se puede editar")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            eventosService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}