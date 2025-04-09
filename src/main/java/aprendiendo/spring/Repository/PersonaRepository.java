package aprendiendo.spring.Repository;

import aprendiendo.spring.Models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Integer> {

    Optional<Persona> findByEmail(String email);

}
