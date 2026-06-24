package com.jugador_progreso.jugador_progreso.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jugador_progreso.jugador_progreso.Modelo.Progreso;

@Repository
public interface ProgresoRepository  extends JpaRepository<Progreso, Long>{


}
