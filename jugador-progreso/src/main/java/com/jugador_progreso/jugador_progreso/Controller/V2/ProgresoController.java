package com.jugador_progreso.jugador_progreso.Controller.V2;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
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

import com.jugador_progreso.jugador_progreso.Assemblers.ProgresoModelAssembler;
import com.jugador_progreso.jugador_progreso.DTO.ProgresoDTO;
import com.jugador_progreso.jugador_progreso.Modelo.Progreso;
import com.jugador_progreso.jugador_progreso.Service.ProgresoService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("progresoControllerV2")
@RequestMapping("/api/v2/progresos")
@Validated
public class ProgresoController {

    @Autowired
    private ProgresoService progresoService;

    @Autowired
    private ProgresoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> listarTodos() {
        try {
            List<ProgresoDTO> progresos = progresoService.listarTodos();

            List<EntityModel<ProgresoDTO>> progresosModel = progresos.stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            if (progresosModel.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                    progresosModel,
                    linkTo(methodOn(ProgresoController.class).listarTodos()).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            ProgresoDTO dto = progresoService.obtenerPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> crearProgreso(@Valid @RequestBody Progreso progreso) {
        try {
            ProgresoDTO dto = progresoService.guardarProgreso(progreso);
            return ResponseEntity
                    .created(linkTo(methodOn(ProgresoController.class).obtenerPorId(dto.getIdProgreso())).toUri())
                    .body(assembler.toModel(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> actualizarProgreso(@PathVariable Long id, @Valid @RequestBody ProgresoDTO datosNuevos) {
        try {
            // El request body y el service usan ProgresoDTO mapeados de forma correcta
            ProgresoDTO dto = progresoService.actualizarProgreso(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No se puede actualizar")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> editarProgreso(@PathVariable Long id, @RequestBody ProgresoDTO datosNuevos) {
        try {
            ProgresoDTO dto = progresoService.editarProgreso(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No se puede editar")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            progresoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}