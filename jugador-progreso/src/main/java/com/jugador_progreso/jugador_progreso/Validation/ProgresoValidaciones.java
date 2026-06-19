package com.jugador_progreso.jugador_progreso.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jugador_progreso.jugador_progreso.Modelo.Progreso;
import com.jugador_progreso.jugador_progreso.Repository.ProgresoRepository;

@Component
public class ProgresoValidaciones {

    @Autowired
    private ProgresoRepository progresoRepository;

    public Boolean validarNullSinNada(Progreso progreso) {
        if (progreso == null) {
            return false;
        }
        if (progreso.getJugador() == null || progreso.getJugador().getIdJugador() == null) {
            return false;
        }
        if (progreso.getNivelActual() < 1) {
            return false;
        }
        if (progreso.getPuntosExperiencia() < 0) {
            return false;
        }
        return true;
    }

    public Boolean existeEnBaseDatos(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return progresoRepository.existsById(id);
    }
    
}