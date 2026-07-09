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

import com.Mansion.HabitacionesMC.DTO.ObjetoDTO;
import com.Mansion.HabitacionesMC.Model.Objeto;
import com.Mansion.HabitacionesMC.Repository.ObjetoRepository;
import com.Mansion.HabitacionesMC.Service.ObjetoService;
import com.Mansion.HabitacionesMC.Validation.ObjetoValidaciones;

@SpringBootTest
public class ObjetoServiceTest {

    @Autowired
    private ObjetoService objetoService;

    @MockitoBean
    private ObjetoRepository objetoRepository;

    @MockitoBean
    private ObjetoValidaciones validaciones;

    private Objeto crearObjetoBase() {
        return new Objeto(1L, "Chipote Chillon", "Martillo de ule que hace más daño qe el Mnojir", "Martillo", 10);
    }

    @Test
    public void testListarTodo() {
        when(objetoRepository.findAll()).thenReturn(List.of(crearObjetoBase()));

        List<ObjetoDTO> objetos = objetoService.listarTodo();

        assertNotNull(objetos);
        assertEquals(1, objetos.size());
    }

    @Test
    public void testObtenerPorId() {
        when(objetoRepository.findById(1L)).thenReturn(Optional.of(crearObjetoBase()));

        ObjetoDTO objeto = objetoService.obtenerPorId(1L);

        assertNotNull(objeto);
        assertEquals("Chipote Chillon", objeto.getNombre());
    }

    @Test
    public void testBuscarPorNombre() {
        when(objetoRepository.findByNombreContainingIgnoreCase("Martillo")).thenReturn(List.of(crearObjetoBase()));

        List<ObjetoDTO> objetos = objetoService.buscarPorNombre("Martillo");

        assertNotNull(objetos);
        assertEquals(1, objetos.size());
        assertEquals("Chipote Chillon", objetos.get(0).getNombre());
    }

    @Test
    public void testBuscarPorTipo() {
        when(objetoRepository.findByTipoObjeto("Martillo")).thenReturn(List.of(crearObjetoBase()));

        List<ObjetoDTO> objetos = objetoService.buscarPorTipo("Martillo");

        assertNotNull(objetos);
        assertEquals(1, objetos.size());
    }

    @Test
    public void testGuardarObjeto() {
        Objeto objeto = crearObjetoBase();
        when(validaciones.validarNullSinNada(objeto)).thenReturn(true);
        when(objetoRepository.save(objeto)).thenReturn(objeto);

        ObjetoDTO guardado = objetoService.guardarObjeto(objeto);

        assertNotNull(guardado);
        assertEquals("Chipote Chillon", guardado.getNombre());
    }

    @Test
    public void testActualizarObjeto() {
        Objeto existente = crearObjetoBase();
        ObjetoDTO datosNuevos = new ObjetoDTO();
        datosNuevos.setNombre("Espada Ceremonial");
        datosNuevos.setDescripcion("Una espada especial mata maldiciones, propiedad: Yuta Okkotsu");
        datosNuevos.setTipoObjeto("Arma");
        datosNuevos.setValorBase(500);

        when(validaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(objetoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(objetoRepository.save(existente)).thenReturn(existente);

        ObjetoDTO actualizado = objetoService.actualizarObjeto(1L, datosNuevos);

        assertNotNull(actualizado);
        assertEquals("Espada Ceremonial", actualizado.getNombre());
        assertEquals(500, actualizado.getValorBase());
    }

    @Test
    public void testEditarObjeto() {
        Objeto existente = crearObjetoBase();
        ObjetoDTO patchData = new ObjetoDTO();
        patchData.setValorBase(25);

        when(validaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(objetoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(objetoRepository.save(existente)).thenReturn(existente);

        ObjetoDTO editado = objetoService.editarObjeto(1L, patchData);

        assertNotNull(editado);
        assertEquals("Chipote Chillon", editado.getNombre());
        assertEquals(25, editado.getValorBase());
    }

    @Test
    public void testEliminarObjeto() {
        when(validaciones.existeEnBaseDatos(1L)).thenReturn(true);
        doNothing().when(objetoRepository).deleteById(1L);

        objetoService.eliminarObjeto(1L);

        verify(objetoRepository, times(1)).deleteById(1L);
    }

}
