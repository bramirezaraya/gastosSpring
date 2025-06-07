package aprendiendo.spring.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
@Data
public class PrestadoDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    public String nombre;
    @NotNull(message = "La fecha no puede estar vacía")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public LocalDate fecha;
    @NotNull(message = "La persona no puede estar vacía")
    public Integer idPersona;
    @NotNull(message = "La cantidad de dinero no puede estar vacía")
    public double dinero;
    @NotBlank(message = "La descripción no puede estar vacía")
    public String descripcion;

    public PrestadoDTO(String nombre, LocalDate fecha, Integer idPersona, double dinero, String descripcion) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.idPersona = idPersona;
        this.dinero = dinero;
        this.descripcion = descripcion;
    }

    public PrestadoDTO() {
    }
}
