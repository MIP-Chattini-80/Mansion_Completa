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

import com.mansion_2.personajes.Assemblers.PersonajesModelAssembler;
import com.mansion_2.personajes.Model.Personajes;
import com.mansion_2.personajes.dto.PersonajesDTO;
import com.mansion_2.personajes.service.PersonajesService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("personajesControllerV2")
@RequestMapping("/api/v2/categoria-personajes")
@Validated
public class PersonajesController {

    @Autowired
    private PersonajesService personajesService;

    @Autowired
    private PersonajesModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<PersonajesDTO>>> listarTodo() {
        try {
            List<EntityModel<PersonajesDTO>> categorias = personajesService.listarTodo().stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            if (categorias.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                    categorias,
                    linkTo(methodOn(PersonajesController.class).listarTodo()).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PersonajesDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            PersonajesDTO dto = personajesService.obtenerPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PersonajesDTO>> guardarCategorias(@Valid @RequestBody Personajes personajes) {
        try {
            PersonajesDTO guardado = personajesService.guardarPersonajes(personajes);
            return ResponseEntity
                    .created(linkTo(methodOn(PersonajesController.class).obtenerPorId(guardado.getId())).toUri())
                    .body(assembler.toModel(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PersonajesDTO>> actualizarCategorias(@PathVariable Long id,
            @Valid @RequestBody PersonajesDTO datosNuevos) {
        try {
            PersonajesDTO actualizado = personajesService.actualizarPersonajes(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PersonajesDTO>> editarCategorias(@PathVariable Long id,
            @RequestBody PersonajesDTO datosNuevos) {
        try {
            PersonajesDTO editado = personajesService.editarPersonajes(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(editado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategorias(@PathVariable Long id) {
        try {
            personajesService.eliminarPersonajes(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
