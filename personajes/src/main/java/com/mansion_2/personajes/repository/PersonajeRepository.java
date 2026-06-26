package com.mansion_2.personajes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mansion_2.personajes.Model.Personaje;

public interface PersonajeRepository extends JpaRepository<Personaje, Long> {

    List<Personaje> findByNombreContainingIgnoreCase(String nombre);

    List<Personaje> findByTipoOrigenIgnoreCase(String tipoOrigen);

    boolean existsByNombreIgnoreCaseAndOrigenIgnoreCase(String nombre, String origen);

    boolean existsByPersonajesId(Long idPersonajes);
}