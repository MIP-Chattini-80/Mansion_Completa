package com.mansion_2.personajes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mansion_2.personajes.Model.Personajes;
import com.mansion_2.personajes.repository.PersonajeRepository;
import com.mansion_2.personajes.repository.PersonajesRepository;

@Service
public class PersonajesValidaciones {

    @Autowired
    private PersonajesRepository personajesRepository;

    @Autowired
    private PersonajeRepository personajeRepository;

    public void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
    }

    public void validarPersonajes(Personajes personajes) {
        if (personajes == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }

        limpiarCategoria(personajes.getCategoria());
    }

    public String limpiarCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría no puede estar vacía");
        }

        String limpia = categoria.trim().replaceAll("\\s+", " ");

        if (!limpia.equalsIgnoreCase("Aliado") && !limpia.equalsIgnoreCase("Antagonista")) {
            throw new IllegalArgumentException("La categoría solo puede ser Aliado o Antagonista");
        }

        if (limpia.length() < 6 || limpia.length() > 20) {
            throw new IllegalArgumentException("La categoría debe tener entre 6 y 20 caracteres");
        }

        return limpia;
    }

    public void validarDuplicado(String categoria) {
        if (personajesRepository.findByCategoriaIgnoreCase(categoria) != null) {
            throw new RuntimeException("Ya existe esa categoría");
        }
    }

    public void validarDuplicadoAlActualizar(String categoria, Long id) {
        Personajes duplicado = personajesRepository.findByCategoriaIgnoreCase(categoria);

        if (duplicado != null && !duplicado.getId().equals(id)) {
            throw new RuntimeException("Ya existe otra categoría con ese nombre");
        }
    }

    public void validarCategoriaSinPersonajes(Long id) {
        if (personajeRepository.existsByPersonajesId(id)) {
            throw new RuntimeException("No se puede eliminar una categoría que tiene personajes asociados");
        }
    }
}