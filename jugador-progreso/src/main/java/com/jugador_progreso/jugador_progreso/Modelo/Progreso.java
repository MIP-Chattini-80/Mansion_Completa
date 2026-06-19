package com.jugador_progreso.jugador_progreso.Modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PROGRESO")
public class Progreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProgreso;
    
    @OneToOne
    @JoinColumn(name = "id_jugador", nullable = false)
    private Jugador jugador;

    @Min(value = 1, message = "El nivel mínimo es 1")
    @Column(name = "nivel_actual")
    private int nivelActual;

    @Min(value = 0, message = "La experiencia no puede ser negativa")
    @Column(name = "puntos_experiencia")
    private int puntosExperiencia;


}