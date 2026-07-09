package com.Mansion.HabitacionesMC.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Mansion.HabitacionesMC.Model.Puerta;
import com.Mansion.HabitacionesMC.Repository.PuertaRepository;

@Component
public class PuertaValidaciones {

    @Autowired
    private PuertaRepository puertaRepository;

    public Boolean validarNullSinNada(Puerta puertaNueva) {
        if (puertaNueva == null) {
            return false;
        }
        if (puertaNueva.getOrigen() == null || puertaNueva.getOrigen().getIdHabitacion() == null) {
            return false;
        }
        if (puertaNueva.getDestino() == null || puertaNueva.getDestino().getIdHabitacion() == null) {
            return false;
        }
        return true;
    }

    public Boolean existeEnBaseDatos(Long id) {
        if (id == null || id <= 0) { /* verifica si el id es nulo o no existe */
            return false;
        }
        return puertaRepository.existsById(id); /* consulta final de que esté en la base de datos */
    }

}
