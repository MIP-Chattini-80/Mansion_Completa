package com.jugador_progreso.jugador_progreso.Controller.V1;

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

import com.jugador_progreso.jugador_progreso.DTO.ProgresoDTO;
import com.jugador_progreso.jugador_progreso.Modelo.Progreso;
import com.jugador_progreso.jugador_progreso.Service.ProgresoService;

import jakarta.validation.Valid;

@RestController("progresoControllerV1")
@RequestMapping("/api/v1/progresos")
@Validated
public class ProgresoController {

    @Autowired
    private ProgresoService progresoService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<ProgresoDTO> progresos = progresoService.listarTodos();
            return ResponseEntity.ok(progresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            ProgresoDTO dto = progresoService.obtenerPorId(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> guardarProgreso(@Valid @RequestBody Progreso progreso) {
        try {
            ProgresoDTO guardado = progresoService.guardarProgreso(progreso);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al vincular o guardar los datos de progreso.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProgreso(@PathVariable Long id, @Valid @RequestBody ProgresoDTO datosNuevos) {
        try {
            ProgresoDTO actualizado = progresoService.actualizarProgreso(id, datosNuevos);
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
    public ResponseEntity<?> editarProgreso(@PathVariable Long id, @RequestBody ProgresoDTO datosNuevos) {
        try {
            ProgresoDTO editado = progresoService.editarProgreso(id, datosNuevos);
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
            progresoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
}