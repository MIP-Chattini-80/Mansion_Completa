package com.mansion_2.personajes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mansion_2.personajes.Model.Personajes;

public interface PersonajesRepository extends JpaRepository<Personajes, Long> {

    Personajes findByCategoriaIgnoreCase(String categoria);
}