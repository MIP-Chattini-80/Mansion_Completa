package com.mansion_2.personajes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.mansion_2.personajes.Model.Personajes;
import com.mansion_2.personajes.dto.PersonajesDTO;
import com.mansion_2.personajes.repository.PersonajesRepository;
import com.mansion_2.personajes.validation.PersonajesValidaciones;

@SpringBootTest
public class PersonajesServiceTest {

    @Autowired
    private PersonajesService personajesService;

    @MockitoBean
    private PersonajesRepository personajesRepository;

    @MockitoBean
    private PersonajesValidaciones validaciones;

    private Personajes categoriaDePrueba() {
        return new Personajes(1L, "Aliado");
    }

    @Test
    public void testListarTodo() {
        when(personajesRepository.findAll()).thenReturn(List.of(categoriaDePrueba()));

        List<PersonajesDTO> categorias = personajesService.listarTodo();

        assertNotNull(categorias);
        assertEquals(1, categorias.size());
    }

    @Test
    public void testObtenerPorId() {
        when(validaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(personajesRepository.findById(1L)).thenReturn(Optional.of(categoriaDePrueba()));

        PersonajesDTO categoria = personajesService.obtenerPorId(1L);

        assertNotNull(categoria);
        assertEquals("Aliado", categoria.getCategoria());
    }

    @Test
    public void testGuardarPersonajes() {
        Personajes categoria = categoriaDePrueba();
        when(validaciones.validarNullSinNada(categoria)).thenReturn(true);
        when(validaciones.existeCategoria("Aliado")).thenReturn(false);
        when(personajesRepository.save(categoria)).thenReturn(categoria);

        PersonajesDTO savedCategoria = personajesService.guardarPersonajes(categoria);

        assertNotNull(savedCategoria);
        assertEquals("Aliado", savedCategoria.getCategoria());
    }

    @Test
    public void testActualizarPersonajes() {
        Personajes existente = categoriaDePrueba();
        PersonajesDTO datosNuevos = new PersonajesDTO();
        datosNuevos.setCategoria("Antagonista");

        when(validaciones.validarNullSinNada(any(Personajes.class))).thenReturn(true);
        when(validaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(personajesRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(personajesRepository.findByCategoriaIgnoreCase("Antagonista")).thenReturn(null);
        when(personajesRepository.save(existente)).thenReturn(existente);

        PersonajesDTO actualizado = personajesService.actualizarPersonajes(1L, datosNuevos);

        assertNotNull(actualizado);
        assertEquals("Antagonista", actualizado.getCategoria());
    }

    @Test
    public void testEditarPersonajes() {
        Personajes existente = categoriaDePrueba();
        PersonajesDTO patchData = new PersonajesDTO();
        patchData.setCategoria("Antagonista");

        when(validaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(personajesRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(personajesRepository.save(existente)).thenReturn(existente);

        PersonajesDTO editedCategoria = personajesService.editarPersonajes(1L, patchData);

        assertNotNull(editedCategoria);
        assertEquals("Antagonista", editedCategoria.getCategoria());
    }

    @Test
    public void testEliminarPersonajes() {
        when(validaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(validaciones.categoriaTienePersonajes(1L)).thenReturn(false);
        doNothing().when(personajesRepository).deleteById(1L);

        personajesService.eliminarPersonajes(1L);

        verify(personajesRepository, times(1)).deleteById(1L);
    }

}