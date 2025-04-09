package aprendiendo.spring.initializer;

import aprendiendo.spring.Models.CategoriaGasto;
import aprendiendo.spring.Repository.CategoriaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (categoriaRepository.count() == 0) {
            categoriaRepository.save(new CategoriaGasto("Super Mercado"));
            categoriaRepository.save(new CategoriaGasto("Transporte"));
            categoriaRepository.save(new CategoriaGasto("Luz"));
            categoriaRepository.save(new CategoriaGasto("Gustos Personales"));
            categoriaRepository.save(new CategoriaGasto("Viajes"));
        }
    }
}

