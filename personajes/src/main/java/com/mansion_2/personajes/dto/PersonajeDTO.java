package com.mansion_2.personajes.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonajeDTO {

    private Long id;
    private String nombre;
    private String origen;
    private String tipoOrigen;
    private Long idPersonajesPersonajes;
    private ObjetoExternoDTO objetoAsignado;

}