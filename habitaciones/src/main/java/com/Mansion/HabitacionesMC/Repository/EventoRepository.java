package com.Mansion.HabitacionesMC.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Mansion.HabitacionesMC.Model.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    
    
}
