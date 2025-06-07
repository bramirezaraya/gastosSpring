package aprendiendo.spring.Services;

import aprendiendo.spring.Models.GastoDTO;
import aprendiendo.spring.Models.Gastos;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDate;

public interface ServicioGastos {
    ObjectNode saveGasto(GastoDTO gasto);
    ObjectNode getGasto(int id);
    ObjectNode updateGasto(Gastos gasto);
    ObjectNode deleteGasto(int id);
    ObjectNode getGastos(LocalDate fechaInicio, LocalDate fechaFin, int idPersona, int pageNumber, int size);
    ObjectNode gastosPorCategoria(int idPersona, LocalDate fechaInicio, LocalDate fechaFin);
    ObjectNode ultimosGastos(int idPersona);
    ObjectNode gastosAhorrosPorMes(int idPersona, int year);
}
