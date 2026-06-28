package com.Mansion.HabitacionesMC.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Mansion.HabitacionesMC.Model.Objeto;
import com.Mansion.HabitacionesMC.Repository.ObjetoRepository;

@Component
public class ObjetoValidaciones {

    @Autowired
    private ObjetoRepository objetoRepository;

    public Boolean validarNullSinNada(Objeto objeto) {
        if (objeto == null) {
            return false;
        }
        if (objeto.getNombre() == null || objeto.getNombre().trim().isEmpty()) {
            return false;
        }
        if (objeto.getDescripcion() == null || objeto.getDescripcion().trim().isEmpty()) {
            return false;
        }
        if (objeto.getTipoObjeto() == null || objeto.getTipoObjeto().trim().isEmpty()) {
            return false;
        }
        if (objeto.getValorBase() == null || objeto.getValorBase() < 0) {
            return false;
        }
        return true;
    }

    public Boolean existeEnBaseDatos(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return objetoRepository.existsById(id);
    }

}
