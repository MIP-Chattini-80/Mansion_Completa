package com.Mansion.HabitacionesMC.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Mansion.HabitacionesMC.Model.Objeto;

@Repository
public interface ObjetoRepository extends JpaRepository<Objeto, Long> {
    
    List<Objeto> findByNombreContainingIgnoreCase(String nombre); /* Lista de objetos por nombre */

    List<Objeto> findByTipoObjeto(String tipoObjeto); /* lista de objetos por tipo */
/*clase tipoObjeto puede creaRSE */
}
