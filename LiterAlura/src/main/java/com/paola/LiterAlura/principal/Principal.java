package com.paola.LiterAlura.principal;

import com.paola.LiterAlura.model.*;
import com.paola.LiterAlura.repository.AutorRepository;
import com.paola.LiterAlura.repository.LibroRepository;
import com.paola.LiterAlura.service.ConsumoAPI;
import com.paola.LiterAlura.service.ConvierteDatos;

import java.util.*;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final Scanner teclado = new Scanner(System.in);

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }


    public void muestraElMenu() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("""
                Elija una opción:
                1 - Buscar libro por título
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en un determinado año
                5 - Listar libros por idioma
                0 - Salir
            """);

            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
                continue;
            }

            switch (opcion) {
                case 1 -> buscarLibro();
                case 2 -> listarLibros();
                case 3 -> listarAutores();
                case 4 -> {
                    System.out.print("Ingrese el año para buscar autores vivos: ");
                    try {
                        int año = Integer.parseInt(teclado.nextLine());
                        listarVivos(año);
                    } catch (NumberFormatException e) {
                        System.out.println("Año inválido. Debe ingresar un número.");
                    }
                }
                case 5 -> listarLibroPorIdioma();
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción inválida. Intente de nuevo.");
            }
        }

        teclado.close();
    }

    private void buscarLibro() {
        System.out.print("Escribe el nombre del libro que deseas buscar: ");
        String nombreLibro = teclado.nextLine().trim();

        // ¿Ya existe un libro con ese título?
        Optional<Libro> libroExistente = libroRepository.findByTituloIgnoreCase(nombreLibro);
        if (libroExistente.isPresent()) {
            System.out.println("Ese libro ya está registrado en la base de datos:");
            System.out.println(libroExistente.get());
            return;
        }

        // Si no existe, buscar en la API
        String urlBusqueda = URL_BASE + "?search=" + nombreLibro.replace(" ", "+");
        String json = consumoApi.obtenerDatos(urlBusqueda);
        Datos datos = conversor.obtenerDatos(json, Datos.class);

        if (datos.resultados().isEmpty()) {
            System.out.println("No se encontró el libro con ese título.");
        } else {
            DatosLibros datosLibro = datos.resultados().get(0);

            // Convertimos idiomas
            List<Idioma> idiomas = datosLibro.idiomas().stream()
                    .map(codigo -> {
                        try {
                            return Idioma.valueOf(codigo.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();

            // Evitar duplicados de autores
            List<Autor> autores = new ArrayList<>();
            for (DatosAutor datosAutor : datosLibro.autor()) {
                Optional<Autor> autorExistente = autorRepository.findByNombreIgnoreCase(datosAutor.Nombre());
                Autor autor = autorExistente.orElseGet(() -> {
                    Autor nuevo = new Autor(
                            datosAutor.Nombre(),
                            datosAutor.FechaDeNacimiento(),
                            datosAutor.FechaDeFallecimiento()
                    );
                    return autorRepository.save(nuevo);
                });
                autores.add(autor);
            }

            // Crear el libro con autores e idiomas
            Libro libro = new Libro(datosLibro.Titulo(), datosLibro.numeroDeDescargas(), idiomas);
            libro.setAutores(autores);

            libroRepository.save(libro);
            System.out.println("\nLibro guardado exitosamente:");
            System.out.println(libro);
        }
    }



    private void listarLibros() {
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
        } else {
            System.out.println("\nLibros registrados:");
            libros.forEach(System.out::println);
        }
    }
    private void listarAutores() {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
        } else {
            System.out.println("\nAutores registrados:");
            autores.forEach(System.out::println);
        }
    }

    private void listarVivos(int año) {
        List<Autor> autores = autorRepository.findAll();

        List<Autor> vivos = autores.stream()
                .filter(autor ->
                        autor.getFechaDeNacimiento() != null &&
                                autor.getFechaDeNacimiento() <= año &&
                                (autor.getFechaDeFallecimiento() == null || autor.getFechaDeFallecimiento() > año)
                )
                .toList();

        if (vivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + año + ".");
        } else {
            System.out.println("\nAutores vivos en el año " + año + ":");
            vivos.forEach(System.out::println);
        }
    }
    private void listarLibroPorIdioma() {
        System.out.println("Idiomas disponibles:");
        for (Idioma idioma : Idioma.values()) {
            System.out.printf("%s - %s%n", idioma.name(), idioma.getDescripcion());
        }

        System.out.print("Ingrese el código de idioma: ");
        String entrada = teclado.nextLine().trim().toUpperCase();

        try {
            Idioma idiomaBuscado = Idioma.valueOf(entrada);

            List<Libro> libros = libroRepository.findAll();

            List<Libro> filtrados = libros.stream()
                    .filter(libro -> libro.getIdiomas().contains(idiomaBuscado))
                    .toList();

            if (filtrados.isEmpty()) {
                System.out.println("No se encontraron libros en el idioma: " + idiomaBuscado.getDescripcion());
            } else {
                System.out.println("\nLibros en " + idiomaBuscado.getDescripcion() + ":");
                filtrados.forEach(System.out::println);
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Código de idioma no válido. Intente nuevamente.");
        }
    }

}

