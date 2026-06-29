package com.Mansion.HabitacionesMC.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Mansion.HabitacionesMC.Model.Dialogo;

public interface DialogoRepository extends JpaRepository<Dialogo, Long> {

    List<Dialogo> findByCodigoBloqueOrderByOrdenAsc(String codigoBloque);

    Dialogo findByCodigoBloqueAndOrden(String codigoBloque, Integer orden);

}
