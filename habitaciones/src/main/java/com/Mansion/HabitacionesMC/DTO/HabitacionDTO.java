package com.Mansion.HabitacionesMC.DTO;

import lombok.Data;

@Data
public class HabitacionDTO {

    private Long idHabitacion;
    private String nombre;
    private String descripcion;
    private boolean esZonaSegura;

}
