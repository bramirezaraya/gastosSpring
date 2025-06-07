package aprendiendo.spring.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
public class Prestado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    @NotBlank(message = "El nombre no puede estar vacío")
    public String nombre;
    @NotBlank(message = "La descripción no puede estar vacía")
    public String descripcion;
    @NotNull(message = "La cantidad de dinero no puede estar vacía")
    public double dinero;
    @NotNull(message = "La fecha no puede estar vacía")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public LocalDate fecha;
    @NotNull(message = "La persona no puede estar vacía")
    @ManyToOne
    @JoinColumn(name = "persona")
    public Persona persona;

    public Prestado(String nombre, double dinero, LocalDate fecha, Persona persona, String descripcion) {
        this.nombre = nombre;
        this.dinero = dinero;
        this.fecha = fecha;
        this.persona = persona;
        this.descripcion = descripcion;
    }

    public Prestado() {
    }
}
