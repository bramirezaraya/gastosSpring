package aprendiendo.spring.Repository;

import aprendiendo.spring.Models.Ahorros;
import aprendiendo.spring.Models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AhorroRepository extends JpaRepository<Ahorros, Integer> {

    Page<Ahorros> findByPersonaOrderByFecha(Persona persona, Pageable pageable);
    List<Ahorros> findByPersona(Persona persona);
    Page<Ahorros> findByFechaBetweenAndPersonaOrderByFecha(LocalDate fechaInicio, LocalDate fechaFin, Persona persona, Pageable pageable);
    List<Ahorros> findByFechaBetweenAndPersona( LocalDate fechaInicio, LocalDate fechaFin, Persona persona);
}
