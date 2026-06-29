package com.Mansion.HabitacionesMC.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Mansion.HabitacionesMC.Model.Evento;
import com.Mansion.HabitacionesMC.Repository.EventoRepository;

@Component
public class EventoValidaciones {

    @Autowired
    private EventoRepository eventoRepository;

    public Boolean validarNullSinNada(Evento evento) {
        if (evento == null) {
            return false;
        }
        if (evento.getTipoEvento() == null || evento.getTipoEvento().getIdEventos() == null) {
            return false;
        }
        if (evento.getDescripcionEspecifica() == null || evento.getDescripcionEspecifica().trim().isEmpty()) {
            return false;
        }
        if (evento.getHabitacion() == null) {
            return false;
        }
        return true;
    }

    public Boolean existeEnBaseDatos(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return eventoRepository.existsById(id);
    }

}
