package com.Mansion.HabitacionesMC.DTO;

import lombok.Data;

@Data
public class EventoDTO {

    private Long idEvento;
    private Long idTipoEvento;
    private Long idHabitacion;
    private String descripcionEspecifica;
    private boolean completado;

}
