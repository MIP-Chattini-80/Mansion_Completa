package com.Mansion.HabitacionesMC.Controller.V1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Mansion.HabitacionesMC.Model.Dialogo;
import com.Mansion.HabitacionesMC.Model.Partida;
import com.Mansion.HabitacionesMC.Service.PartidaService;

@RestController
@RequestMapping("/api/v1/partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

    @GetMapping("/crear/{idJugador}")
    public Partida crearPartida(@PathVariable Long idJugador) {
        return partidaService.crearPartida(idJugador);
    }

    @GetMapping("/{idPartida}/dialogo")
    public Dialogo obtenerDialogoActual(@PathVariable Long idPartida) {
        return partidaService.obtenerDialogoActual(idPartida);
    }

    @GetMapping("/{idPartida}/avanzar")
    public Dialogo avanzarDialogo(@PathVariable Long idPartida) {
        return partidaService.avanzarDialogo(idPartida);
    }

    @GetMapping("/{idPartida}/nombre")
    public Partida guardarNombreJugador(@PathVariable Long idPartida, @RequestParam String nombreJugador) {
        return partidaService.guardarNombreJugador(idPartida, nombreJugador);
    }
}
