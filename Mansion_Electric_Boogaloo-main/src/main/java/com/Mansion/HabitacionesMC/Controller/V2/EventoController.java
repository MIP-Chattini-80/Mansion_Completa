package com.Mansion.HabitacionesMC.Controller.V2;

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

import com.Mansion.HabitacionesMC.Assemblers.EventoModelAssembler;
import com.Mansion.HabitacionesMC.DTO.EventoDTO;
import com.Mansion.HabitacionesMC.Model.Evento;
import com.Mansion.HabitacionesMC.Service.EventoService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("eventoControllerV2")
@RequestMapping("/api/v2/eventos")
@Validated
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private EventoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> listarEventosDTO() {
        try {
            List<EventoDTO> eventos = eventoService.listarEventosDTO();

            List<EntityModel<EventoDTO>> eventosModel = eventos.stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            if (eventosModel.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CollectionModel.of(
                    eventosModel,
                    linkTo(methodOn(EventoController.class).listarEventosDTO()).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            // El servicio ya retorna un EventoDTO directamente
            EventoDTO dto = eventoService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> guardarEvento(@Valid @RequestBody Evento evento) {
        try {
            // El servicio guarda la entidad física pero te retorna el DTO limpio
            EventoDTO dto = eventoService.guardarEvento(evento);
            return ResponseEntity
                    .created(linkTo(methodOn(EventoController.class).buscarPorId(dto.getIdEvento())).toUri())
                    .body(assembler.toModel(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> actualizarEvento(@PathVariable Long id, @Valid @RequestBody Evento eventoDetalles) {
        try {
            // Recibe entidad física para actualizar, pero retorna EventoDTO
            EventoDTO dto = eventoService.actualizarEvento(id, eventoDetalles);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> editarEvento(@PathVariable Long id, @RequestBody Evento datosNuevos) {
        try {
            // Recibe entidad física para parchar campos, pero retorna EventoDTO
            EventoDTO dto = eventoService.editarEvento(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            eventoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}