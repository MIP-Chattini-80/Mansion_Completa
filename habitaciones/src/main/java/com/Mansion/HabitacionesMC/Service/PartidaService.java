package com.Mansion.HabitacionesMC.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Mansion.HabitacionesMC.Model.Dialogo;
import com.Mansion.HabitacionesMC.Model.Partida;
import com.Mansion.HabitacionesMC.Repository.DialogoRepository;
import com.Mansion.HabitacionesMC.Repository.PartidaRepository;

@Service
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private DialogoRepository dialogoRepository;

    public Partida crearPartida(Long idJugador) {

        Partida partida = new Partida();

        partida.setIdJugador(idJugador);
        partida.setBloqueActual("INTRO_NOMBRE");
        partida.setLineaActual(1);
        partida.setTerminada(false);
        partida.setEsperandoRespuesta(true);

        return partidaRepository.save(partida);
    }

    public Dialogo obtenerDialogoActual(Long idPartida) {

        Partida partida = partidaRepository.findById(idPartida).get();

        return dialogoRepository.findByCodigoBloqueAndOrden(
                partida.getBloqueActual(),
                partida.getLineaActual()
        );
    }

    public Dialogo avanzarDialogo(Long idPartida) {

        Partida partida = partidaRepository.findById(idPartida).get();

        if (partida.getTerminada() == true || partida.getEsperandoRespuesta() == true) {
            return dialogoRepository.findByCodigoBloqueAndOrden(
                    partida.getBloqueActual(),
                    partida.getLineaActual()
            );
        }

        Dialogo dialogoActual = dialogoRepository.findByCodigoBloqueAndOrden(
                partida.getBloqueActual(),
                partida.getLineaActual()
        );

        if (dialogoActual.getSiguienteBloque() != null) {
            partida.setBloqueActual(dialogoActual.getSiguienteBloque());
            partida.setLineaActual(1);
        } else {
            partida.setLineaActual(partida.getLineaActual() + 1);
        }

        Dialogo nuevoDialogo = dialogoRepository.findByCodigoBloqueAndOrden(
                partida.getBloqueActual(),
                partida.getLineaActual()
        );

        if (nuevoDialogo == null) {
            partida.setTerminada(true);
            partidaRepository.save(partida);
            return dialogoActual;
        }

        partidaRepository.save(partida);
        return nuevoDialogo;
    }

    public Partida guardarNombreJugador(Long idPartida, String nombreJugador) {

        Partida partida = partidaRepository.findById(idPartida).get();

        if (nombreJugador == null || nombreJugador.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede estar vacío.");
        }

        partida.setEsperandoRespuesta(false);
        partida.setBloqueActual("INTRO_DESPERTAR");
        partida.setLineaActual(1);

        return partidaRepository.save(partida);
    }
}
