package com.Mansion.HabitacionesMC.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.Mansion.HabitacionesMC.DTO.HabitacionDTO;
import com.Mansion.HabitacionesMC.Model.Habitacion;
import com.Mansion.HabitacionesMC.Repository.HabitacionRepository;
import com.Mansion.HabitacionesMC.Service.HabitacionService;
import com.Mansion.HabitacionesMC.Validation.HabitacionValidaciones;

@SpringBootTest
public class HabitacionServiceTest {

    @Autowired
    private HabitacionService habitacionService;

    @MockitoBean
    private HabitacionRepository habitacionRepository;

    @MockitoBean
    private HabitacionValidaciones habitacionValidaciones;

    private Habitacion crearHabitacionBase() {
        return new Habitacion(1L, "Biblioteca",
                "Una sala llena de libros antiguos y polvorientos custodiada por Lisa de Genshin Impact", false);
    }

    @Test
    public void testListarTodas() {
        when(habitacionRepository.findAll()).thenReturn(List.of(crearHabitacionBase()));

        List<HabitacionDTO> habitaciones = habitacionService.listarTodas();

        assertNotNull(habitaciones);
        assertEquals(1, habitaciones.size());
    }

    @Test
    public void testObtenerPorId() {
        when(habitacionValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(crearHabitacionBase()));

        HabitacionDTO habitacion = habitacionService.obtenerPorId(1L);

        assertNotNull(habitacion);
        assertEquals("Biblioteca", habitacion.getNombre());
    }

    @Test
    public void testBuscarPorNombre() {
        when(habitacionRepository.findByNombreIgnoreCase("Biblioteca")).thenReturn(crearHabitacionBase());

        HabitacionDTO habitacion = habitacionService.buscarPorNombre("Biblioteca");

        assertNotNull(habitacion);
        assertEquals("Biblioteca", habitacion.getNombre());
    }

    @Test
    public void testGuardarHabitacion() {
        Habitacion habitacion = crearHabitacionBase();
        when(habitacionValidaciones.validarNullSinNada(habitacion)).thenReturn(true);
        when(habitacionRepository.findByNombreIgnoreCase("Biblioteca")).thenReturn(null);
        when(habitacionRepository.save(habitacion)).thenReturn(habitacion);

        HabitacionDTO guardada = habitacionService.guardarHabitacion(habitacion);

        assertNotNull(guardada);
        assertEquals("Biblioteca", guardada.getNombre());
    }

    @Test
    public void testGuardarHabitacionDuplicada() {
        Habitacion habitacion = crearHabitacionBase();
        when(habitacionValidaciones.validarNullSinNada(habitacion)).thenReturn(true);
        when(habitacionRepository.findByNombreIgnoreCase("Biblioteca")).thenReturn(crearHabitacionBase());

        assertThrows(RuntimeException.class, () -> habitacionService.guardarHabitacion(habitacion));
    }

    @Test
    public void testActualizarHabitacion() {
        Habitacion existente = crearHabitacionBase();
        HabitacionDTO datosNuevos = new HabitacionDTO();
        datosNuevos.setNombre("Salón de Té");
        datosNuevos.setDescripcion(
                "Una acogedora sala con aroma a hierbas y porcelana fina, donde está sentada Echidna de Re:Zero");
        datosNuevos.setEsZonaSegura(true);

        when(habitacionValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(habitacionRepository.findByNombreIgnoreCase("Salón de Té")).thenReturn(null);
        when(habitacionRepository.save(existente)).thenReturn(existente);

        HabitacionDTO actualizada = habitacionService.actualizarHabitacion(1L, datosNuevos);

        assertNotNull(actualizada);
        assertEquals("Salón de Té", actualizada.getNombre());
        assertEquals(true, actualizada.isEsZonaSegura());
    }

    @Test
    public void testEditarHabitacion() {
        Habitacion existente = crearHabitacionBase();
        HabitacionDTO patchData = new HabitacionDTO();
        patchData.setEsZonaSegura(true);

        when(habitacionValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(habitacionRepository.save(existente)).thenReturn(existente);

        HabitacionDTO editada = habitacionService.editarHabitacion(1L, patchData);

        assertNotNull(editada);
        assertEquals("Biblioteca", editada.getNombre());
        assertEquals(true, editada.isEsZonaSegura());
    }

    @Test
    public void testEliminar() {
        when(habitacionValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        doNothing().when(habitacionRepository).deleteById(1L);

        habitacionService.eliminar(1L);

        verify(habitacionRepository, times(1)).deleteById(1L);
    }

}
