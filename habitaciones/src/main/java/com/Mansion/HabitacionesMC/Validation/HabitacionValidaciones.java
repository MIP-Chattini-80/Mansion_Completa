package com.Mansion.HabitacionesMC.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Mansion.HabitacionesMC.Model.Habitacion;
import com.Mansion.HabitacionesMC.Repository.HabitacionRepository;

@Component
public class HabitacionValidaciones {

    @Autowired
    private HabitacionRepository habitacionRepository;

    public Boolean validarNullSinNada(Habitacion habitacion) {
        if (habitacion == null) {
            return false;
        }
        if (habitacion.getNombre() == null || habitacion.getNombre().trim().isEmpty()) {
            return false;
        }
        if (habitacion.getDescripcion() == null || habitacion.getDescripcion().trim().length() < 10 || habitacion.getDescripcion().trim().length() > 255) {
            return false;
        }
        return true;
    }

    public Boolean existeEnBaseDatos(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return habitacionRepository.existsById(id);
    }

}
