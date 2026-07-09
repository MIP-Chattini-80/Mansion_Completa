package com.mansion_2.personajes.controller.V1;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.mansion_2.personajes.dto.PersonajeDTO;
import com.mansion_2.personajes.Model.Personaje;
import com.mansion_2.personajes.service.PersonajeService;
import jakarta.validation.Valid;

@RestController("personajeControllerV1")
@RequestMapping("/api/v1/personaje")
@Validated
public class PersonajeController {

    @Autowired
    private PersonajeService personajeService;

    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<PersonajeDTO> lista = personajeService.listarTodo();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            PersonajeDTO dto = personajeService.obtenerPorId(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/buscar-nombre")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre) {
        try {
            List<PersonajeDTO> resultados = personajeService.buscarPorNombre(nombre);
            return ResponseEntity.ok(resultados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/buscar-origen")
    public ResponseEntity<?> buscarPorTipoOrigen(@RequestParam String tipoOrigen) {
        try {
            List<PersonajeDTO> resultados = personajeService.buscarPorTipoOrigen(tipoOrigen);
            return ResponseEntity.ok(resultados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> guardarPersonaje(@Valid @RequestBody Personaje personaje) {
        try {
            PersonajeDTO guardado = personajeService.guardarPersonaje(personaje);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error de integridad de datos al guardar el personaje.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPersonaje(@PathVariable Long id, @Valid @RequestBody Personaje personaje) {
        try {
            PersonajeDTO actualizado = personajeService.actualizarPersonaje(id, personaje);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editarPersonaje(@PathVariable Long id, @RequestBody PersonajeDTO personaje) {
        try {
            PersonajeDTO editado = personajeService.editarPersonaje(id, personaje);
            return ResponseEntity.ok(editado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPersonaje(@PathVariable Long id) {
        try {
            personajeService.eliminarPersonaje(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}