package com.paola.LiterAlura.repository;

import com.paola.LiterAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombre(String nombre);

    Optional<Autor> findByNombreIgnoreCase(String nombre);
}
