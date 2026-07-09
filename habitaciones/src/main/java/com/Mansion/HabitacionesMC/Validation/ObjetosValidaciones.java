package com.Mansion.HabitacionesMC.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Mansion.HabitacionesMC.Model.Objetos;
import com.Mansion.HabitacionesMC.Repository.ObjetosRepository;

@Component
public class ObjetosValidaciones {

    @Autowired
    private ObjetosRepository objetosRepository;

    public Boolean validarNullSinNada(Objetos objetos) {
        if (objetos == null) {
            return false;
        }
        if (objetos.getObjetoBase() == null || objetos.getObjetoBase().getIdObjeto() == null) {
            return false;
        }
        if (objetos.getUbicacion() == null) {
            return false;
        }
        if (objetos.getEstado() == null || objetos.getEstado().trim().isEmpty()) {
            return false;
        }
        if (objetos.getCantidad() == null || objetos.getCantidad() < 0) {
            return false;
        }
        return true;
    }

    public Boolean existeEnBaseDatos(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return objetosRepository.existsById(id);
    }

}
