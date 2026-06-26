package com.mansion_2.personajes.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mansion_2.personajes.Model.Personajes;
import com.mansion_2.personajes.repository.PersonajeRepository;
import com.mansion_2.personajes.repository.PersonajesRepository;

@Component
public class PersonajesValidaciones {

    @Autowired
    private PersonajesRepository personajesRepository;

    @Autowired
    private PersonajeRepository personajeRepository;

    public Boolean validarNullSinNada(Personajes personajes) {
        if (personajes == null) {
            return false;
        }

        if (personajes.getCategoria() == null || personajes.getCategoria().trim().isEmpty()) {
            return false;
        }

        String categoria = personajes.getCategoria().trim();

        if (categoria.equalsIgnoreCase("Aliado") == false
                && categoria.equalsIgnoreCase("Antagonista") == false) {
            return false;
        }

        return true;
    }

    public Boolean existeEnBaseDatos(Long id) {
        if (id == null || id <= 0) {
            return false;
        }

        return personajesRepository.existsById(id);
    }

    public Boolean existeCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return false;
        }

        Personajes encontrado = personajesRepository.findByCategoriaIgnoreCase(categoria.trim());

        if (encontrado == null) {
            return false;
        }

        return true;
    }

    public Boolean categoriaTienePersonajes(Long id) {
        if (id == null || id <= 0) {
            return false;
        }

        return personajeRepository.existsByPersonajesId(id);
    }
}