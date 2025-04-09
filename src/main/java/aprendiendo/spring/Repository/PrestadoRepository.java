package aprendiendo.spring.Repository;

import aprendiendo.spring.Models.Persona;
import aprendiendo.spring.Models.Prestado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface PrestadoRepository extends JpaRepository<Prestado, Integer> {

    Page<Prestado> findByPersonaOrderByFecha(Persona persona, Pageable page);
    Page<Prestado> findByFechaBetweenAndPersonaOrderByFecha(LocalDate fechaInicio, LocalDate fechaFin, Persona persona, Pageable page);
}
