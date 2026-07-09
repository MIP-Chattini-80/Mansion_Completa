package com.Mansion.HabitacionesMC.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Mansion.HabitacionesMC.Model.Eventos;
import com.Mansion.HabitacionesMC.Repository.EventosRepository;


@Component
public class EventosValidaciones {

    @Autowired
    private EventosRepository eventosRepository;

    public Boolean validarNullSinNada(Eventos eventos){
        if (eventos == null) {
            return false;
        }
        if (eventos.getCategoria() == null || eventos.getCategoria().trim().isEmpty()) {
            return false;
        }
        if(eventos.getExperienciaBase() <= 0){
            return false;
        }
        return true;
    }

    public Boolean existeEnBaseDatos(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return eventosRepository.existsById(id);
    }

}
