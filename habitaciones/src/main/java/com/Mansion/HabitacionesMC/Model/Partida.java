package com.Mansion.HabitacionesMC.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "partida")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partida")
    private Long idPartida;

    @Column(name = "id_jugador")
    private Long idJugador;

    @Column(name = "bloque_actual")
    private String bloqueActual;

    @Column(name = "linea_actual")
    private Integer lineaActual;

    @Column(name = "terminada")
    private Boolean terminada;

    @Column(name = "esperando_respuesta")
    private Boolean esperandoRespuesta;
}
