package aprendiendo.spring.Services;

import aprendiendo.spring.Models.AhorrosDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDate;

public interface ServicioAhorro {
    ObjectNode getAhorros(LocalDate fechaInicio, LocalDate fechaFin, int idPersona, int pageNumber, int size);
    ObjectNode getAhorro(int id);
    ObjectNode deleteAhorro(int id);
    ObjectNode saveAhorro(AhorrosDTO ahorro);
    ObjectNode updateAhorro(int id, AhorrosDTO ahorro);
}
