package com.mansion_2.personajes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.mansion_2.personajes.Model.Personaje;
import com.mansion_2.personajes.Model.Personajes;
import com.mansion_2.personajes.dto.ObjetoExternoDTO;
import com.mansion_2.personajes.dto.PersonajeDTO;
import com.mansion_2.personajes.repository.PersonajeRepository;
import com.mansion_2.personajes.validation.PersonajeValidaciones;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class PersonajeServiceTest {

    @Autowired
    private PersonajeService personajeService;

    @MockitoBean
    private PersonajeRepository personajeRepository;

    @MockitoBean
    private PersonajeValidaciones validaciones;

    private Personaje personajeDePrueba() {
        return new Personaje(1L, "Satoru Gojo", "Jujutsu Kaisen", "Anime", new Personajes(1L, "Aliado"), null);
    }

    @Test
    public void testListarTodo() {
        when(personajeRepository.findAll()).thenReturn(List.of(personajeDePrueba()));
        when(validaciones.obtenerObjeto(null)).thenReturn(new ObjetoExternoDTO());

        List<PersonajeDTO> personajes = personajeService.listarTodo();

        assertNotNull(personajes);
        assertEquals(1, personajes.size());
    }

    @Test
    public void testObtenerPorId() {
        when(personajeRepository.findById(1L)).thenReturn(Optional.of(personajeDePrueba()));
        when(validaciones.obtenerObjeto(null)).thenReturn(new ObjetoExternoDTO());

        PersonajeDTO personaje = personajeService.obtenerPorId(1L);

        assertNotNull(personaje);
        assertEquals("Satoru Gojo", personaje.getNombre());
    }

    @Test
    public void testGuardarPersonaje() {
        Personaje personaje = personajeDePrueba();
        when(validaciones.validarNullSinNada(personaje)).thenReturn(true);
        when(personajeRepository.save(personaje)).thenReturn(personaje);
        when(validaciones.obtenerObjeto(null)).thenReturn(new ObjetoExternoDTO());

        PersonajeDTO savedPersonaje = personajeService.guardarPersonaje(personaje);

        assertNotNull(savedPersonaje);
        assertEquals("Satoru Gojo", savedPersonaje.getNombre());
    }

    @Test
    public void testActualizarPersonaje() {
        Personaje existente = personajeDePrueba();
        Personaje datosNuevos = new Personaje(null, "Utahime Iori", "Jujutsu Kaisen", "Anime",
                new Personajes(1L, "Aliado"), null);

        when(validaciones.validarNullSinNada(datosNuevos)).thenReturn(true);
        when(validaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(personajeRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(personajeRepository.save(existente)).thenReturn(existente);
        when(validaciones.obtenerObjeto(null)).thenReturn(new ObjetoExternoDTO());

        PersonajeDTO actualizado = personajeService.actualizarPersonaje(1L, datosNuevos);

        assertNotNull(actualizado);
        assertEquals("Utahime Iori", actualizado.getNombre());
    }

    @Test
    public void testEditarPersonaje() {
        Personaje existente = personajeDePrueba();
        PersonajeDTO patchData = new PersonajeDTO();
        patchData.setNombre("Mei Mei");

        when(validaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(personajeRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(personajeRepository.save(existente)).thenReturn(existente);
        when(validaciones.obtenerObjeto(null)).thenReturn(new ObjetoExternoDTO());

        PersonajeDTO editedPersonaje = personajeService.editarPersonaje(1L, patchData);

        assertNotNull(editedPersonaje);
        assertEquals("Mei Mei", editedPersonaje.getNombre());
    }

    @Test
    public void testEliminarPersonaje() {
        when(validaciones.existeEnBaseDatos(1L)).thenReturn(true);
        doNothing().when(personajeRepository).deleteById(1L);

        personajeService.eliminarPersonaje(1L);

        verify(personajeRepository, times(1)).deleteById(1L);
    }

}
