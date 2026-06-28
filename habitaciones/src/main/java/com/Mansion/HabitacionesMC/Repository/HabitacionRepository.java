package com.Mansion.HabitacionesMC.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Mansion.HabitacionesMC.Model.Habitacion;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    Habitacion findByNombreIgnoreCase(String nombre);

}
