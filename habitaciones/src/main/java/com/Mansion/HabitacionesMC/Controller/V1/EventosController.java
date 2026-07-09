package com.Mansion.HabitacionesMC.Controller.V1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.Mansion.HabitacionesMC.DTO.EventosDTO;
import com.Mansion.HabitacionesMC.Model.Eventos;
import com.Mansion.HabitacionesMC.Service.EventosService;

import jakarta.validation.Valid;

@RestController("eventosControllerV1")
@RequestMapping("/api/v1/categorias-eventos")
@Validated
public class EventosController {

    @Autowired
    private EventosService eventosService;

    @GetMapping
    public ResponseEntity<?> listarCategorias() {
        try {
            List<EventosDTO> categorias = eventosService.listarCategorias();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            EventosDTO categoria = eventosService.obtenerPorId(id);
            return ResponseEntity.ok(categoria);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> guardarInstancia(@Valid @RequestBody Eventos eventos) {
        try {
            EventosDTO guardado = eventosService.guardarInstancia(eventos);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarInstancia(@PathVariable Long id, @Valid @RequestBody EventosDTO datosNuevos) {
        try {
            EventosDTO actualizado = eventosService.actualizarInstancia(id, datosNuevos);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No se puede actualizar")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody EventosDTO datosNuevos) {
        try {
            EventosDTO editado = eventosService.editar(id, datosNuevos);
            return ResponseEntity.ok(editado);
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
            eventosService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); /* Error 400: enviaste la cosa mal loco */
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); /* Error 404: no encontrado */
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); /* Error 500: tu wa es mala, esta cancion es mala! */
        }
    }

}
