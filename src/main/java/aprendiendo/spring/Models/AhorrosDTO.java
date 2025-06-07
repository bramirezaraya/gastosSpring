package aprendiendo.spring.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
@Data
public class AhorrosDTO {
    @NotBlank(message = "El nombre es requerido")
    private String nombre;
    @NotNull(message = "El monto es requerido")
    private double monto;
    @NotNull(message = "La fecha es requerida")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate fecha;
    @NotNull(message = "La persona es requerida")
    private Integer id_persona;

}
