package com.Mansion.HabitacionesMC.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Mansion.HabitacionesMC.Model.Dialogo;
import com.Mansion.HabitacionesMC.Repository.DialogoRepository;

@Service
public class DialogoService {

    @Autowired
    private DialogoRepository dialogoRepository;

    public List<Dialogo> buscarPorBloque(String codigoBloque) {
        return dialogoRepository.findByCodigoBloqueOrderByOrdenAsc(codigoBloque);
    }
}
