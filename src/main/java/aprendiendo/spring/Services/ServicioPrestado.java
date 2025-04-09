package aprendiendo.spring.Services;

import aprendiendo.spring.Models.PrestadoDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDate;

public interface ServicioPrestado {
    ObjectNode getPrestados(int idPersona, LocalDate fechaInicio, LocalDate fechaFin, int page, int size);
    ObjectNode deletePrestado(int idPrestamo);
    ObjectNode savePrestado(PrestadoDTO prestado);
}
