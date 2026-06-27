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

import com.jugador_progreso.jugador_progreso.Assemblers.JugadorModelAssembler;
import com.jugador_progreso.jugador_progreso.DTO.JugadorDTO;
import com.jugador_progreso.jugador_progreso.Modelo.Jugador;
import com.jugador_progreso.jugador_progreso.Service.JugadorService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("jugadorControllerV2")
@RequestMapping("/api/v2/jugador")
@Validated
public class JugadorController {

    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private JugadorModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> listarTodos() {
        try {
            List<JugadorDTO> jugadores = jugadorService.listarTodos();

            List<EntityModel<JugadorDTO>> jugadoresModel = jugadores.stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            if (jugadoresModel.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                    jugadoresModel,
                    linkTo(methodOn(JugadorController.class).listarTodos()).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            JugadorDTO dto = jugadorService.obtenerPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> guardarJugador(@Valid @RequestBody Jugador jugador) {
        try {
            JugadorDTO dto = jugadorService.guardarJugador(jugador);
            return ResponseEntity
                    .created(linkTo(methodOn(JugadorController.class).obtenerPorId(dto.getIdJugador())).toUri())
                    .body(assembler.toModel(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> actualizarJugador(@PathVariable Long id, @RequestBody JugadorDTO datosNuevos) {
        try {
            JugadorDTO dto = jugadorService.actualizarJugador(id, datosNuevos);
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
    public ResponseEntity<?> editarJugador(@PathVariable Long id, @RequestBody JugadorDTO datosNuevos) {
        try {
            JugadorDTO dto = jugadorService.editarJugador(id, datosNuevos);
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
            jugadorService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}