package com.mansion_2.personajes.controller.V2;

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

import com.mansion_2.personajes.Assemblers.PersonajeModelAssembler;
import com.mansion_2.personajes.Model.Personaje;
import com.mansion_2.personajes.dto.PersonajeDTO;
import com.mansion_2.personajes.service.PersonajeService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("personajeControllerV2")
@RequestMapping("/api/v2/personaje")
@Validated
public class PersonajeController {

    @Autowired
    private PersonajeService personajeService;

    @Autowired
    private PersonajeModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<PersonajeDTO>>> listarTodo() {
        try {
            List<EntityModel<PersonajeDTO>> personajes = personajeService.listarTodo().stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            if (personajes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                    personajes,
                    linkTo(methodOn(PersonajeController.class).listarTodo()).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PersonajeDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            PersonajeDTO dto = personajeService.obtenerPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/nombre/{nombre}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<PersonajeDTO>>> buscarPorNombre(@PathVariable String nombre) {
        try {
            List<EntityModel<PersonajeDTO>> personajes = personajeService.buscarPorNombre(nombre).stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            if (personajes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                    personajes,
                    linkTo(methodOn(PersonajeController.class).buscarPorNombre(nombre)).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/tipo-origen/{tipoOrigen}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<PersonajeDTO>>> buscarPorTipoOrigen(
            @PathVariable String tipoOrigen) {
        try {
            List<EntityModel<PersonajeDTO>> personajes = personajeService.buscarPorTipoOrigen(tipoOrigen).stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            if (personajes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(CollectionModel.of(
                    personajes,
                    linkTo(methodOn(PersonajeController.class).buscarPorTipoOrigen(tipoOrigen)).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PersonajeDTO>> guardarPersonaje(@Valid @RequestBody Personaje personaje) {
        try {
            PersonajeDTO guardado = personajeService.guardarPersonaje(personaje);
            return ResponseEntity
                    .created(linkTo(methodOn(PersonajeController.class).obtenerPorId(guardado.getId())).toUri())
                    .body(assembler.toModel(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PersonajeDTO>> actualizarPersonaje(@PathVariable Long id,
            @Valid @RequestBody Personaje personaje) {
        try {
            PersonajeDTO actualizado = personajeService.actualizarPersonaje(id, personaje);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PersonajeDTO>> editarPersonaje(@PathVariable Long id,
            @RequestBody PersonajeDTO personaje) {
        try {
            PersonajeDTO editado = personajeService.editarPersonaje(id, personaje);
            return ResponseEntity.ok(assembler.toModel(editado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPersonaje(@PathVariable Long id) {
        try {
            personajeService.eliminarPersonaje(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
