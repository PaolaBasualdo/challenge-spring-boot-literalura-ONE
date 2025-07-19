package com.paola.LiterAlura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {

    public String obtenerDatos(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (IOException e) {
            System.out.println("Error al conectarse a la API. Verifique su conexión a Internet.");
            return null;

        } catch (InterruptedException e) {
            System.out.println("La solicitud fue interrumpida.");
            Thread.currentThread().interrupt();
            return null;

        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado al consumir la API: " + e.getMessage());
            return null;
        }
    }
}
