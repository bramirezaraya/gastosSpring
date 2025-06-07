package aprendiendo.spring.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
public class Ahorros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "El nombre es requerido")
    private String nombre;
    @NotNull(message = "El monto es requerido")
    private double monto;
    @NotNull(message = "La fecha es requerida")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "idPersona")
    private Persona persona;

    public Ahorros(String nombre, double monto, LocalDate fecha, Persona persona) {
        this.nombre = nombre;
        this.monto = monto;
        this.fecha = fecha;
        this.persona = persona;
    }

    public Ahorros() {

    }
}
