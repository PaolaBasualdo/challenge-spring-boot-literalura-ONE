package com.paola.LiterAlura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos {

    private ObjectMapper mapper = new ObjectMapper();
    //Usa ObjectMapper de Jackson para convertir el String JSON en
    // una instancia del record DatosSerie.

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        //Este método es genérico, lo que le permite convertir el JSON en cualquier
        // tipo de clase que le pases como segundo parámetro.
        try {
            return mapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}