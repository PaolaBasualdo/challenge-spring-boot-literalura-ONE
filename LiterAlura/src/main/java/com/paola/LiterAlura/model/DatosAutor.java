package com.paola.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(
        @JsonAlias("name") String Nombre,
        @JsonAlias("birth_year") Integer FechaDeNacimiento,
        @JsonAlias("death_year") Integer FechaDeFallecimiento
        ) {
}
