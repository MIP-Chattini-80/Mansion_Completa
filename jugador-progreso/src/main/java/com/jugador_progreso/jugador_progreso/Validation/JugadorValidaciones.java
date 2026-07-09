package com.jugador_progreso.jugador_progreso.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jugador_progreso.jugador_progreso.Modelo.Jugador;
import com.jugador_progreso.jugador_progreso.Repository.JugadorRepository;

@Component
public class JugadorValidaciones {

    @Autowired
    private JugadorRepository jugadorRepository;

    public Boolean validarNullSinNada(Jugador jugador) {
        if (jugador == null) {
            return false;
        }
        if (jugador.getUsername() == null || jugador.getUsername().trim().isEmpty()) {
            return false;
        }
        if (jugador.getPassword() == null || jugador.getPassword().trim().isEmpty()) {
            return false;
        }
        if (jugador.getEmail() == null || jugador.getEmail().trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public Boolean existeEnBaseDatos(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return jugadorRepository.existsById(id);
    }
}