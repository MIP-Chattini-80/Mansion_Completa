package com.Mansion.HabitacionesMC.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Mansion.HabitacionesMC.Model.Eventos;

@Repository
public interface EventosRepository extends JpaRepository<Eventos, Long> {
    
    Eventos findByCategoriaIgnoreCase(String categoria);
    
}
