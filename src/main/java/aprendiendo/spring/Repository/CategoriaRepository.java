package aprendiendo.spring.Repository;

import aprendiendo.spring.Models.CategoriaGasto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<CategoriaGasto, Integer> {

    boolean existsByNombre(String nombre);
}
