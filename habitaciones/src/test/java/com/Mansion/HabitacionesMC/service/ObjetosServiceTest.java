package com.Mansion.HabitacionesMC.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.Mansion.HabitacionesMC.DTO.ObjetosDTO;
import com.Mansion.HabitacionesMC.Model.Habitacion;
import com.Mansion.HabitacionesMC.Model.Objeto;
import com.Mansion.HabitacionesMC.Model.Objetos;
import com.Mansion.HabitacionesMC.Repository.ObjetosRepository;
import com.Mansion.HabitacionesMC.Service.ObjetosService;
import com.Mansion.HabitacionesMC.Validation.ObjetosValidaciones;

@SpringBootTest
public class ObjetosServiceTest {

    @Autowired
    private ObjetosService objetosService;

    @MockitoBean
    private ObjetosRepository objetosRepository;

    @MockitoBean
    private ObjetosValidaciones objetosValidaciones;

    private Objetos crearObjetosBase() {
        Objeto objetoBase = new Objeto(1L, "Llave Oxidada", "Una vieja llave de hierro cubierta de óxido", "Llave", 10);
        Habitacion ubicacion = new Habitacion(1L, "Biblioteca", "Una sala llena de libros antiguos y polvorientos",
                false);
        return new Objetos(1L, objetoBase, ubicacion, "Activo", 3);
    }

    @Test
    public void testListarTodo() {
        when(objetosRepository.findAll()).thenReturn(List.of(crearObjetosBase()));

        List<ObjetosDTO> objetos = objetosService.listarTodo();

        assertNotNull(objetos);
        assertEquals(1, objetos.size());
    }

    @Test
    public void testObtenerPorId() {
        when(objetosValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(objetosRepository.findById(1L)).thenReturn(Optional.of(crearObjetosBase()));

        ObjetosDTO objeto = objetosService.obtenerPorId(1L);

        assertNotNull(objeto);
        assertEquals("Llave Oxidada", objeto.getNombreObjeto());
        assertEquals("Biblioteca", objeto.getNombreHabitacion());
    }

    @Test
    public void testBuscarPorHabitacion() {
        when(objetosRepository.findByUbicacionIdHabitacion(1L)).thenReturn(List.of(crearObjetosBase()));

        List<ObjetosDTO> objetos = objetosService.buscarPorHabitacion(1L);

        assertNotNull(objetos);
        assertEquals(1, objetos.size());
    }

    @Test
    public void testGuardar() {
        Objetos objetos = crearObjetosBase();
        when(objetosValidaciones.validarNullSinNada(objetos)).thenReturn(true);
        when(objetosRepository.save(objetos)).thenReturn(objetos);

        ObjetosDTO guardado = objetosService.guardar(objetos);

        assertNotNull(guardado);
        assertEquals("Activo", guardado.getEstado());
    }

    @Test
    public void testActualizar() {
        Objetos existente = crearObjetosBase();
        ObjetosDTO datosNuevos = new ObjetosDTO();
        datosNuevos.setEstado("Roto");
        datosNuevos.setCantidad(0);

        when(objetosValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(objetosRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(objetosRepository.save(existente)).thenReturn(existente);

        ObjetosDTO actualizado = objetosService.actualizar(1L, datosNuevos);

        assertNotNull(actualizado);
        assertEquals("Roto", actualizado.getEstado());
        assertEquals(0, actualizado.getCantidad());
    }

    @Test
    public void testEditar() {
        Objetos existente = crearObjetosBase();
        ObjetosDTO patchData = new ObjetosDTO();
        patchData.setCantidad(5);

        when(objetosValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(objetosRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(objetosRepository.save(existente)).thenReturn(existente);

        ObjetosDTO editado = objetosService.editar(1L, patchData);

        assertNotNull(editado);
        assertEquals("Activo", editado.getEstado());
        assertEquals(5, editado.getCantidad());
    }

    @Test
    public void testEliminar() {
        when(objetosValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        doNothing().when(objetosRepository).deleteById(1L);

        objetosService.eliminar(1L);

        verify(objetosRepository, times(1)).deleteById(1L);
    }

}