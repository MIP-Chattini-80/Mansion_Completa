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

import com.Mansion.HabitacionesMC.Assemblers.ObjetoModelAssembler;
import com.Mansion.HabitacionesMC.DTO.ObjetoDTO;
import com.Mansion.HabitacionesMC.Model.Objeto;
import com.Mansion.HabitacionesMC.Service.ObjetoService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("objetoControllerV2")
@RequestMapping("/api/v2/objeto")
@Validated
public class ObjetoController {

    @Autowired
    private ObjetoService objetoService;

    @Autowired
    private ObjetoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ObjetoDTO>>> listarTodo() {
        try {
            List<EntityModel<ObjetoDTO>> objetos = objetoService.listarTodo().stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            if (objetos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                    objetos,
                    linkTo(methodOn(ObjetoController.class).listarTodo()).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ObjetoDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            ObjetoDTO dto = objetoService.obtenerPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/nombre/{nombre}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ObjetoDTO>>> buscarPorNombre(@PathVariable String nombre) {
        try {
            List<EntityModel<ObjetoDTO>> objetos = objetoService.buscarPorNombre(nombre).stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            if (objetos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                    objetos,
                    linkTo(methodOn(ObjetoController.class).buscarPorNombre(nombre)).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/tipo/{tipoObjeto}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ObjetoDTO>>> buscarPorTipoObjeto(
            @PathVariable String tipoObjeto) {
        try {
            List<EntityModel<ObjetoDTO>> objetos = objetoService.buscarPorTipo(tipoObjeto).stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            if (objetos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                    objetos,
                    linkTo(methodOn(ObjetoController.class).buscarPorTipoObjeto(tipoObjeto)).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ObjetoDTO>> guardarObjeto(@Valid @RequestBody Objeto objeto) {
        try {
            ObjetoDTO guardado = objetoService.guardarObjeto(objeto);
            return ResponseEntity
                    .created(linkTo(methodOn(ObjetoController.class).obtenerPorId(guardado.getIdObjeto())).toUri())
                    .body(assembler.toModel(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ObjetoDTO>> actualizarObjeto(@PathVariable Long id,
            @Valid @RequestBody ObjetoDTO datosNuevos) { // CORREGIDO: Recibe ObjetoDTO para alinearse con el Service
        try {
            ObjetoDTO actualizado = objetoService.actualizarObjeto(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ObjetoDTO>> editarObjeto(@PathVariable Long id,
            @RequestBody ObjetoDTO datosNuevos) { // CORREGIDO: Recibe ObjetoDTO tal como tu Service lo define
        try {
            ObjetoDTO editado = objetoService.editarObjeto(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(editado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarObjeto(@PathVariable Long id) {
        try {
            objetoService.eliminarObjeto(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
