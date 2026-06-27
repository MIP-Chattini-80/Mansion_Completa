package com.jugador_progreso.jugador_progreso.DTO;

import lombok.Data;

@Data
public class ProgresoDTO {

    private Long idProgreso;
    private Long idJugador;
    private int nivelActual;
    private int puntosExperiencia;
    
}
