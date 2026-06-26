package com.mansion_2.personajes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.mansion_2.personajes.dto.PersonajesDTO;
import com.mansion_2.personajes.Model.Personajes;
import com.mansion_2.personajes.service.PersonajesService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/categoria-personajes")
@Validated
public class PersonajesController {

    @Autowired
    private PersonajesService personajesService;

    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<PersonajesDTO> lista = personajesService.listarTodo();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            PersonajesDTO dto = personajesService.obtenerPorId(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorCategoria(@RequestParam String categoria) {
        try {
            PersonajesDTO dto = personajesService.buscarPorCategoria(categoria);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> guardarCategorias(@Valid @RequestBody Personajes personajes) {
        try {
            PersonajesDTO guardado = personajesService.guardarPersonajes(personajes);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al guardar la categoría o datos duplicados.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCategorias(@PathVariable Long id, @Valid @RequestBody Personajes datosNuevos) {
        try {
            PersonajesDTO actualizado = personajesService.actualizarPersonajes(id, datosNuevos);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editarCategorias(@PathVariable Long id, @RequestBody PersonajesDTO datosNuevos) {
        try {
            PersonajesDTO editado = personajesService.editarPersonajes(id, datosNuevos);
            return ResponseEntity.ok(editado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategorias(@PathVariable Long id) {
        try {
            personajesService.eliminarPersonajes(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}