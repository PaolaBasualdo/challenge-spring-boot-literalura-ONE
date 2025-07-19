package com.paola.LiterAlura.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String titulo;

    private Double numeroDeDescargas;

    @ElementCollection(targetClass = Idioma.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "libro_idiomas", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "idioma")
    @Enumerated(EnumType.STRING)
    private List<Idioma> idiomas;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores;

    public Libro() {
    }

    // Constructor utilizado si ya tenés autores existentes
    public Libro(String titulo, Double numeroDeDescargas, List<Idioma> idiomas, List<Autor> autores) {
        this.titulo = titulo;
        this.numeroDeDescargas = numeroDeDescargas;
        this.idiomas = idiomas;
        this.autores = autores;
    }

    // Constructor que convierte un DatosLibros directamente a entidad Libro
    public Libro(DatosLibros datosLibro) {
        this.titulo = datosLibro.Titulo();
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();

        this.idiomas = datosLibro.idiomas().stream()
                .map(codigo -> {
                    try {
                        return Idioma.valueOf(codigo.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Idioma no reconocido: " + codigo);
                        return null;
                    }
                })
                .filter(idioma -> idioma != null)
                .toList();

        this.autores = datosLibro.autor().stream()
                .map(a -> new Autor(a.Nombre(), a.FechaDeNacimiento(), a.FechaDeFallecimiento()))
                .toList();
    }

    // Constructor alternativo para crear sin autores (por ejemplo, si ya están en la BD)
    public Libro(String titulo, Double numeroDeDescargas, List<Idioma> idiomas) {
        this.titulo = titulo;
        this.numeroDeDescargas = numeroDeDescargas;
        this.idiomas = idiomas;
    }

    @Override
    public String toString() {
        String autoresStr = (autores != null && !autores.isEmpty())
                ? autores.stream().map(Autor::getNombre).collect(Collectors.joining(", "))
                : "Sin autores registrados";

        String idiomasStr = (idiomas != null && !idiomas.isEmpty())
                ? idiomas.stream().map(Idioma::toString).collect(Collectors.joining(", "))
                : "Sin idiomas registrados";

        return "\n------------LIBRO-------------" +
                "\nTítulo: " + titulo +
                "\nAutores: " + autoresStr +
                "\nIdiomas: " + idiomasStr +
                "\nDescargas: " + numeroDeDescargas +
                "\n------------------------------\n";
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public List<Idioma> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<Idioma> idiomas) {
        this.idiomas = idiomas;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }
}
