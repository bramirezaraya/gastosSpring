package aprendiendo.spring.Repository;

import aprendiendo.spring.Models.Gastos;
import aprendiendo.spring.Models.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface GastosRepository extends JpaRepository<Gastos, Integer> {
//    @Query("SELECT g FROM Gastos g WHERE g.idPersona = :idPersona AND g.fecha BETWEEN :fechaInicio AND :fechaFin order by g.fecha")
//    List<Gastos> findByDate(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param("idPersona") int idPersona);
//
//    @Query("SELECT g FROM Gastos g WHERE g.idPersona = :idPersona order by g.fecha")
//    List<Gastos> findByIdPersona(int idPersona);
    Page<Gastos> findByPersonaOrderByFecha(Persona persona, Pageable pageable);
    Page<Gastos> findByPersonaOrderByFechaDesc(Persona persona, Pageable pageable);
    List<Gastos> findByPersona(Persona persona);
    Page<Gastos> findByFechaBetweenAndPersonaOrderByFecha(LocalDate fechaInicio, LocalDate fechaFin, Persona persona, Pageable pageable);
    List<Gastos> findByFechaBetweenAndPersona(LocalDate fechaInicio, LocalDate fechaFin, Persona persona);
}
