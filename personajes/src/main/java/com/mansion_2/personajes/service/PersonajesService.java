package com.mansion_2.personajes.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mansion_2.personajes.Model.Personajes;
import com.mansion_2.personajes.dto.PersonajesDTO;
import com.mansion_2.personajes.repository.PersonajesRepository;
import com.mansion_2.personajes.validation.PersonajesValidaciones;

@Service
public class PersonajesService {

    @Autowired
    private PersonajesRepository personajesRepository;
    
    @Autowired
    private PersonajesValidaciones validaciones;

    private PersonajesDTO convertirADTO(Personajes personajes) {
        PersonajesDTO dto = new PersonajesDTO();

        dto.setId(personajes.getId());
        dto.setCategoria(personajes.getCategoria());

        return dto;
    }

    public List<PersonajesDTO> listarTodo() {
        List<PersonajesDTO> listaDTO = new ArrayList<>();
        List<Personajes> listaPersonajes = personajesRepository.findAll();

        for (Personajes personajes : listaPersonajes) {
            listaDTO.add(convertirADTO(personajes));
        }

        return listaDTO;
    }

    public PersonajesDTO obtenerPorId(Long id) {
        if (validaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("Categoría de personajes no encontrada con el ID: " + id);
        }

        Personajes personajes = personajesRepository.findById(id).get();

        return convertirADTO(personajes);
    }

    public PersonajesDTO buscarPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría a buscar no puede estar vacía.");
        }

        Personajes encontrado = personajesRepository.findByCategoriaIgnoreCase(categoria.trim());

        if (encontrado == null) {
            throw new RuntimeException("No se encontró ninguna categoría con el nombre: " + categoria);
        }

        return convertirADTO(encontrado);
    }

    public PersonajesDTO guardarPersonajes(Personajes personajes) {
        if (validaciones.validarNullSinNada(personajes) == false) {
            throw new IllegalArgumentException("Los datos de la categoría son inválidos o insuficientes.");
        }

        String categoriaLimpia = personajes.getCategoria().trim().replaceAll("\\s+", " ");

        if (validaciones.existeCategoria(categoriaLimpia)) {
            throw new RuntimeException("Ya existe una categoría con ese nombre.");
        }

        personajes.setCategoria(categoriaLimpia);

        Personajes guardado = personajesRepository.save(personajes);

        return convertirADTO(guardado);
    }

    public PersonajesDTO actualizarPersonajes(Long id, Personajes datosNuevos) {
        if (validaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede actualizar. La categoría no existe con el ID: " + id);
        }

        if (validaciones.validarNullSinNada(datosNuevos) == false) {
            throw new IllegalArgumentException("Los datos para actualizar son insuficientes o nulos.");
        }

        Personajes existente = personajesRepository.findById(id).get();

        String categoriaLimpia = datosNuevos.getCategoria().trim().replaceAll("\\s+", " ");

        Personajes categoriaExistente = personajesRepository.findByCategoriaIgnoreCase(categoriaLimpia);

        if (categoriaExistente != null && categoriaExistente.getId().equals(id) == false) {
            throw new RuntimeException("Ya existe otra categoría con ese nombre.");
        }

        existente.setCategoria(categoriaLimpia);

        Personajes actualizado = personajesRepository.save(existente);

        return convertirADTO(actualizado);
    }

    public PersonajesDTO editarPersonajes(Long id, PersonajesDTO datosNuevos) {
        if (validaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede editar. La categoría no existe con el ID: " + id);
        }

        if (datosNuevos == null) {
            throw new IllegalArgumentException("Los datos para editar no pueden ser nulos.");
        }

        Personajes existente = personajesRepository.findById(id).get();

        if (datosNuevos.getCategoria() != null) {
            if (datosNuevos.getCategoria().trim().isEmpty() == false) {
                String categoriaLimpia = datosNuevos.getCategoria().trim().replaceAll("\\s+", " ");

                if (categoriaLimpia.equalsIgnoreCase("Aliado") == false
                        && categoriaLimpia.equalsIgnoreCase("Antagonista") == false) {
                    throw new IllegalArgumentException("La categoría solo puede ser Aliado o Antagonista.");
                }

                existente.setCategoria(categoriaLimpia);
            }
        }

        Personajes actualizado = personajesRepository.save(existente);

        return convertirADTO(actualizado);
    }

    public void eliminarPersonajes(Long id) {
        if (validaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede eliminar. La categoría no existe en la base de datos.");
        }

        if (validaciones.categoriaTienePersonajes(id)) {
            throw new RuntimeException("No se puede eliminar una categoría que tiene personajes asociados.");
        }

        personajesRepository.deleteById(id);
    }
}