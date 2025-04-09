package aprendiendo.spring.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Gastos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idGasto;
    @NotBlank(message = "El nombre es requerido")
    private String nombre;
    @NotNull(message = "El precio es requerido")
    private long precio;
    @NotNull(message = "La cantidad es requerida")
    private int cantidad;
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

    public Gastos(String nombre, long precio, int cantidad, Persona persona, LocalDate fecha, CategoriaGasto categoriaGasto) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.persona = persona;
        this.fecha = fecha;
        this.categoriaGasto = categoriaGasto;
    }

    public Gastos() {
    }

    public long getIdGasto() {
        return idGasto;
    }

    public void setIdGasto(long idGasto) {
        this.idGasto = idGasto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getPrecio() {
        return precio;
    }

    public void setPrecio( long precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona( Persona persona) {
        this.persona = persona;
    }

    public  LocalDate getFecha() {
        return fecha;
    }

    public void setFecha( LocalDate fecha) {
        this.fecha = fecha;
    }

    public  CategoriaGasto getCategoriaGasto() {
        return categoriaGasto;
    }

    public void setCategoriaGasto(CategoriaGasto categoriaGasto) {
        this.categoriaGasto = categoriaGasto;
    }
}
