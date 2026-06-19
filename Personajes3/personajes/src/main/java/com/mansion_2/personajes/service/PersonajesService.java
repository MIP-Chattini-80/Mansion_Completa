package com.mansion_2.personajes.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mansion_2.personajes.dto.PersonajesDTO;
import com.mansion_2.personajes.Model.Personajes;
import com.mansion_2.personajes.repository.PersonajesRepository;
import com.mansion_2.personajes.validation.PersonajesValidaciones;

@Transactional
@Service
public class PersonajesService {

    @Autowired
    private PersonajesRepository personajesRepository;

    @Autowired
    private PersonajesValidaciones validaciones;

    private PersonajesDTO mapToDTO(Personajes personajes) {
        if (personajes == null) {
            return null;
        }
        PersonajesDTO dto = new PersonajesDTO();
        dto.setId(personajes.getId());
        dto.setCategoria(personajes.getCategoria());
        return dto;
    }

    public List<PersonajesDTO> listarTodo() {
        return personajesRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PersonajesDTO obtenerPorId(Long id) {
        if (id == null) throw new IllegalArgumentException("El ID de la categoría no puede ser nulo.");
        return personajesRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Categoría de personajes no encontrada con el ID: " + id));
    }

    // Método de búsqueda personalizado basado en tu PersonajesRepository
    public PersonajesDTO buscarPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría a buscar no puede estar vacía.");
        }
        Personajes encontrado = personajesRepository.findByCategoriaIgnoreCase(categoria.trim());
        if (encontrado == null) {
            throw new RuntimeException("No se encontró ninguna categoría con el nombre: " + categoria);
        }
        return mapToDTO(encontrado);
    }

    public PersonajesDTO guardarPersonajes(Personajes personajes) {
        if (validaciones.validarNullSinNada(personajes) == false) {
            throw new IllegalArgumentException("Los datos de la categoría son inválidos o insuficientes.");
        }
        personajes.setCategoria(personajes.getCategoria().trim().replaceAll("\\s+", " "));
        Personajes guardado = personajesRepository.save(personajes);
        return mapToDTO(guardado);
    }

    public PersonajesDTO actualizarPersonajes(Long id, Personajes datosNuevos) {
        if (id == null || validaciones.validarNullSinNada(datosNuevos) == false) {
            throw new IllegalArgumentException("El ID o los datos para actualizar son insuficientes o nulos.");
        }
        if (validaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede actualizar. La categoría no existe con el ID: " + id);
        }
        Personajes existente = personajesRepository.findById(id).get();
        existente.setCategoria(datosNuevos.getCategoria().trim().replaceAll("\\s+", " "));

        Personajes actualizado = personajesRepository.save(existente);
        return mapToDTO(actualizado);
    }

    public PersonajesDTO editarPersonajes(Long id, PersonajesDTO datosNuevos) {
        if (id == null || datosNuevos == null) {
            throw new IllegalArgumentException("El ID o los datos para editar no pueden ser nulos.");
        }
        if (validaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede editar. La categoría no existe con el ID: " + id);
        }
        
        Personajes existente = personajesRepository.findById(id).get();

        // Lógica de edición fusionada utilizando la estructura == false
        if (datosNuevos.getCategoria() != null) {
            if (datosNuevos.getCategoria().trim().isEmpty() == false) {
                existente.setCategoria(datosNuevos.getCategoria().trim().replaceAll("\\s+", " "));
            }
        }

        Personajes actualizado = personajesRepository.save(existente);
        return mapToDTO(actualizado);
    }

    public void eliminarPersonajes(Long id) {
        if (id == null) throw new IllegalArgumentException("El ID no puede ser nulo.");
        if (validaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede eliminar. La categoría no existe en la base de datos.");
        }
        personajesRepository.deleteById(id);
    }
    
}