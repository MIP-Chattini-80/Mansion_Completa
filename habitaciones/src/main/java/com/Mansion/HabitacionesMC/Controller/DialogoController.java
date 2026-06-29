package com.Mansion.HabitacionesMC.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mansion.HabitacionesMC.Model.Dialogo;
import com.Mansion.HabitacionesMC.Service.DialogoService;

@RestController
@RequestMapping("/api/v1/dialogos")
public class DialogoController {

    @Autowired
    private DialogoService dialogoService;

    @GetMapping("/{codigoBloque}")
    public List<Dialogo> buscarPorBloque(@PathVariable String codigoBloque) {
        return dialogoService.buscarPorBloque(codigoBloque);
    }
}
