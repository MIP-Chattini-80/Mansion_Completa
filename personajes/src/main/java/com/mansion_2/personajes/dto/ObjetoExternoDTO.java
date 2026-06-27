package com.mansion_2.personajes.dto;

import lombok.Data;

@Data
public class ObjetoExternoDTO {

    private Long idObjeto;
    private String nombre;
    private String descripcion;
    private String tipoObjeto;
    private Integer valorBase;

}
