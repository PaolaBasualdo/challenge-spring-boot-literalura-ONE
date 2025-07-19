package com.paola.LiterAlura;
import com.paola.LiterAlura.principal.Principal;
import com.paola.LiterAlura.repository.AutorRepository;
import com.paola.LiterAlura.repository.LibroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {


	private final LibroRepository libroRepository;
	private final AutorRepository autorRepository;

	public LiterAluraApplication(LibroRepository libroRepository, AutorRepository autorRepository) {
		this.libroRepository = libroRepository;
		this.autorRepository = autorRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(libroRepository, autorRepository);
		principal.muestraElMenu();
	}
}
