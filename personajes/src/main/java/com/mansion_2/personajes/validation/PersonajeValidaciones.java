package com.mansion_2.personajes.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.mansion_2.personajes.Model.Personaje;
import com.mansion_2.personajes.dto.ObjetoExternoDTO;
import com.mansion_2.personajes.repository.PersonajeRepository;

import reactor.core.publisher.Mono;

@Component
public class PersonajeValidaciones {

    @Autowired
    private PersonajeRepository personajeRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Boolean validarNullSinNada(Personaje personaje) {
        if (personaje == null) {
            return false;
        }
        if (personaje.getNombre() == null || personaje.getNombre().trim().isEmpty()) {
            return false;
        }
        if (personaje.getOrigen() == null || personaje.getOrigen().trim().isEmpty()) {
            return false;
        }
        if (personaje.getTipoOrigen() == null || personaje.getTipoOrigen().trim().isEmpty()) {
            return false;
        }
        if (personaje.getPersonajes() == null || personaje.getPersonajes().getId() == null) {
            return false;
        }
        return true;
    }

    public ObjetoExternoDTO obtenerObjeto(Long objetoId) {
        ObjetoExternoDTO objetoRecuperado = new ObjetoExternoDTO();

        if (objetoId == null) {
            objetoRecuperado.setIdObjeto(0L);
            objetoRecuperado.setNombre("Sin objeto equipado");
            objetoRecuperado.setDescripcion("El personaje va con las manos vacías.");
            objetoRecuperado.setTipoObjeto("Ninguno");
            objetoRecuperado.setValorBase(0);
            return objetoRecuperado;
        }

        try {
            ObjetoExternoDTO resultado = webClientBuilder.build()
                    .get()
                    .uri("http://mansion-habitaciones/api/v1/objeto/" + objetoId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                    .bodyToMono(ObjetoExternoDTO.class)
                    .block();

            if (resultado != null) {
                return resultado;
            }

            objetoRecuperado.setIdObjeto(0L);
            objetoRecuperado.setNombre("Objeto inexistente");
            objetoRecuperado.setDescripcion("El id de objeto no corresponde a ningún ítem de la mansión.");
            objetoRecuperado.setTipoObjeto("Desconocido");
            objetoRecuperado.setValorBase(0);
            return objetoRecuperado;

        } catch (Exception e) {
            objetoRecuperado.setIdObjeto(0L);
            objetoRecuperado.setNombre("Error de conexión");
            objetoRecuperado.setDescripcion("No se pudo conectar con el microservicio de objetos de la mansión.");
            objetoRecuperado.setTipoObjeto("Desconocido");
            objetoRecuperado.setValorBase(0);
            return objetoRecuperado;
        }
    }

    public Boolean existeEnBaseDatos(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return personajeRepository.existsById(id);
    }

}