package com.mansion_2.personajes.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.mansion_2.personajes.Model.Personajes;
import com.mansion_2.personajes.dto.ObjetoExternoDTO;
import com.mansion_2.personajes.repository.PersonajeRepository;
import com.mansion_2.personajes.repository.PersonajesRepository;

import reactor.core.publisher.Mono;

@Component
public class PersonajesValidaciones {

    @Autowired
    private PersonajesRepository personajesRepository;

    @Autowired
    private PersonajeRepository personajeRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Boolean validarNullSinNada(Personajes personajes) {
        if (personajes == null) {
            return false;
        }

        if (personajes.getCategoria() == null || personajes.getCategoria().trim().isEmpty()) {
            return false;
        }

        String categoria = personajes.getCategoria().trim();

        if (categoria.equalsIgnoreCase("Aliado") == false
                && categoria.equalsIgnoreCase("Antagonista") == false) {
            return false;
        }

        return true;
    }

    public Boolean existeEnBaseDatos(Long id) {
        if (id == null || id <= 0) {
            return false;
        }

        return personajesRepository.existsById(id);
    }

    public Boolean existeCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return false;
        }

        Personajes encontrado = personajesRepository.findByCategoriaIgnoreCase(categoria.trim());

        if (encontrado == null) {
            return false;
        }

        return true;
    }

    public Boolean categoriaTienePersonajes(Long id) {
        if (id == null || id <= 0) {
            return false;
        }

        return personajeRepository.existsByPersonajesId(id);
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

}