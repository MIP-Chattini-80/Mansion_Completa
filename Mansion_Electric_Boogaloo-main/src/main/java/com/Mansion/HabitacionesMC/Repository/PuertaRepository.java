package com.Mansion.HabitacionesMC.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Mansion.HabitacionesMC.Model.Puerta;

@Repository
public interface PuertaRepository extends JpaRepository<Puerta, Long> {

}
