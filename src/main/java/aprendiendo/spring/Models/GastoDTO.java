package aprendiendo.spring.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GastoDTO {
    @NotBlank(message = "El nombre es requerido")
    private String nombre;
    @NotNull(message = "El precio es requerido")
    private long precio;
    @NotNull(message = "La cuota es requerida")
    private int cuotas;
    @NotNull(message = "El id de la persona es requerido")
    private int idPersona;
    @NotNull(message = "la fecha es requerida")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate fecha;
    @NotNull(message = "El id de la categoria es requerido")
    private int idCategoria;

}
