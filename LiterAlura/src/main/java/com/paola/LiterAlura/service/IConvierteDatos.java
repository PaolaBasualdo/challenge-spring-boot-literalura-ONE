package com.paola.LiterAlura.service;

public interface IConvierteDatos {
    //IConvierteDatos.obtenerDatos(String json, Class<T> clase): Este método toma un JSON
    // y una clase como argumentos y convierte el JSON a un objeto de esa clase.
    <T> T obtenerDatos(String json, Class<T> clase);
}