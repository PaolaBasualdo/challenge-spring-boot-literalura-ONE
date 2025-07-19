package com.paola.LiterAlura.model;

public enum Idioma {
    EN("Inglés"),
    ES("Español"),
    FR("Francés"),
    PT("Portugués");

    private String descripcion;

    Idioma(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
