package com.mansion_2.personajes.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mansion_2.personajes.dto.PersonajeDTO;
import com.mansion_2.personajes.Model.Personaje;
import com.mansion_2.personajes.repository.PersonajeRepository;
import com.mansion_2.personajes.validation.PersonajeValidaciones;

@Transactional
@Service
public class PersonajeService {

    @Autowired
    private PersonajeRepository personajeRepository;

    @Autowired
    private PersonajeValidaciones validaciones;

    private PersonajeDTO mapToDTO(Personaje personaje) {
        if (personaje == null) {
            return null;
        }
        PersonajeDTO dto = new PersonajeDTO();
        dto.setId(personaje.getId());
        dto.setNombre(personaje.getNombre());
        dto.setOrigen(personaje.getOrigen());
        dto.setTipoOrigen(personaje.getTipoOrigen());
        
        if (personaje.getPersonajes() != null) {
            dto.setIdPersonajesPersonajes(personaje.getPersonajes().getId());
        }
        return dto;
    }

    public List<PersonajeDTO> listarTodo() {
        return personajeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PersonajeDTO obtenerPorId(Long id) {
        if (id == null) throw new IllegalArgumentException("El ID del personaje no puede ser nulo.");
        return personajeRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Personaje no encontrado con el ID: " + id));
    }

    public List<PersonajeDTO> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Debes colocar un nombre para buscar.");
        }
        return personajeRepository.findByNombreContainingIgnoreCase(nombre.trim()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PersonajeDTO> buscarPorTipoOrigen(String tipoOrigen) {
        if (tipoOrigen == null || tipoOrigen.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de origen no puede estar vacío.");
        }
        return personajeRepository.findByTipoOrigenIgnoreCase(tipoOrigen.trim()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PersonajeDTO guardarPersonaje(Personaje personaje) {
        if (validaciones.validarNullSinNada(personaje) == false) {
            throw new IllegalArgumentException("Los datos del personaje son inválidos o insuficientes.");
        }
        personaje.setNombre(personaje.getNombre().trim().replaceAll("\\s+", " "));
        personaje.setOrigen(personaje.getOrigen().trim().replaceAll("\\s+", " "));
        personaje.setTipoOrigen(personaje.getTipoOrigen().trim().replaceAll("\\s+", " "));
        Personaje guardado = personajeRepository.save(personaje);
        return mapToDTO(guardado);
    }

    public PersonajeDTO actualizarPersonaje(Long id, Personaje personaje) {
        if (id == null || validaciones.validarNullSinNada(personaje) == false) {
            throw new IllegalArgumentException("El ID o los datos para actualizar son insuficientes o nulos.");
        }
        if (validaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede actualizar. El personaje no existe con el ID: " + id);
        }
        Personaje existente = personajeRepository.findById(id).get();
        existente.setNombre(personaje.getNombre().trim().replaceAll("\\s+", " "));
        existente.setOrigen(personaje.getOrigen().trim().replaceAll("\\s+", " "));
        existente.setTipoOrigen(personaje.getTipoOrigen().trim().replaceAll("\\s+", " "));
        existente.setPersonajes(personaje.getPersonajes());

        Personaje actualizado = personajeRepository.save(existente);
        return mapToDTO(actualizado);
    }

    public PersonajeDTO editarPersonaje(Long id, PersonajeDTO personaje) {
        if (id == null || personaje == null) {
            throw new IllegalArgumentException("El ID o los datos para editar no pueden ser nulos.");
        }
        if (validaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede editar. El personaje no existe con el ID: " + id);
        }   
        Personaje existente = personajeRepository.findById(id).get();
        if (personaje.getNombre() != null) {
            if (personaje.getNombre().trim().isEmpty() == false) {
                existente.setNombre(personaje.getNombre().trim().replaceAll("\\s+", " "));
            }
        }
        if (personaje.getOrigen() != null) {
            if (personaje.getOrigen().trim().isEmpty() == false) {
                existente.setOrigen(personaje.getOrigen().trim().replaceAll("\\s+", " "));
            }
        }
        if (personaje.getTipoOrigen() != null) {
            if (personaje.getTipoOrigen().trim().isEmpty() == false) {
                existente.setTipoOrigen(personaje.getTipoOrigen().trim().replaceAll("\\s+", " "));
            }
        }
        Personaje actualizado = personajeRepository.save(existente);
        return mapToDTO(actualizado);
    }

    public void eliminarPersonaje(Long id) {
        if (id == null) throw new IllegalArgumentException("El ID no puede ser nulo.");
        if (validaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede eliminar. El personaje no existe en la base de datos.");
        }
        personajeRepository.deleteById(id);
    }

}