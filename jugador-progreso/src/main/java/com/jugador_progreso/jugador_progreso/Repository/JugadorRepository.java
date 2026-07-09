package com.jugador_progreso.jugador_progreso.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jugador_progreso.jugador_progreso.Modelo.Jugador;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {

}
