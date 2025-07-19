package com.paola.LiterAlura.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private Integer fechaDeNacimiento;

    private Integer fechaDeFallecimiento;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    private List<Libro> libros;


    public Autor() {
    }

    public Autor(String nombre, Integer fechaDeNacimiento, Integer fechaDeFallecimiento) {
        this.nombre = nombre;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    @Override
    public String toString() {
        String librosStr = (libros != null && !libros.isEmpty())
                ? libros.stream()
                .map(Libro::getTitulo)
                .reduce((a, b) -> a + ", " + b)
                .map(str -> "[" + str + "]")
                .orElse("[]")
                : "[]";

        return  "\n----------AUTOR----------" +
                "\nNombre: " + nombre +
                "\nNacimiento: " + (fechaDeNacimiento != null ? fechaDeNacimiento : "¿?") +
                "\nFallecimiento: " + (fechaDeFallecimiento != null ? fechaDeFallecimiento : "¿?") +
                "\nLibros: " + librosStr +
                "\n-------------------------" +
                "\n";
    }


    // Getters y Setters

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
}
