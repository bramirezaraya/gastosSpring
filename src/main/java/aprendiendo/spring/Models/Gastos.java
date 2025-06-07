package aprendiendo.spring.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
public class Gastos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idGasto;
    @NotBlank(message = "El nombre es requerido")
    private String nombre;
    @NotNull(message = "El precio es requerido")
    private long precio;
    @NotNull(message = "Las cuotas es requerida")
    private int cuotas;
    @NotNull(message = "El id de la persona es requerido")
//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idPersona")
    private Persona persona;

    @NotNull(message = "la fecha es requerida")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate fecha;

    @NotNull(message = "El id de la categoria es requerido")
    @ManyToOne
    @JoinColumn(name = "idCategoria")
    private CategoriaGasto categoriaGasto;

    public Gastos(String nombre, long precio, int cuotas, Persona persona, LocalDate fecha, CategoriaGasto categoriaGasto) {
        this.nombre = nombre;
        this.precio = precio;
        this.cuotas = cuotas;
        this.persona = persona;
        this.fecha = fecha;
        this.categoriaGasto = categoriaGasto;
    }

    public Gastos() {
    }
}
