package com.mansion_2.personajes.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mansion_2.personajes.Model.Personaje;
import com.mansion_2.personajes.repository.PersonajeRepository;

@Component
public class PersonajeValidaciones {

    @Autowired
    private PersonajeRepository personajeRepository;

    public Boolean validarNullSinNada(Personaje personaje) {
        if (personaje == null) {
            return false;
        }
        if (personaje.getNombre() == null || personaje.getNombre().trim().isEmpty()) {
            return false;
        }
        if (personaje.getOrigen() == null || personaje.getOrigen().trim().isEmpty()) {
            return false;
        }
        if (personaje.getTipoOrigen() == null || personaje.getTipoOrigen().trim().isEmpty()) {
            return false;
        }
        if (personaje.getPersonajes() == null || personaje.getPersonajes().getId() == null) {
            return false;
        }
        return true;
    }

    public Boolean existeEnBaseDatos(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return personajeRepository.existsById(id);
    }

}