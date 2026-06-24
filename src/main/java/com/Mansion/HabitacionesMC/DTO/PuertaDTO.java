package com.Mansion.HabitacionesMC.DTO;

import lombok.Data;

@Data
public class PuertaDTO {

    private Long idPuerta;
    private Long idHabitacionOrigen;
    private Long idHabitacionDestino;
    private boolean estaBloqueada;

}
