package aprendiendo.spring.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class GastoDTO {
    @NotBlank(message = "El nombre es requerido")
    private String nombre;
    @NotNull(message = "El precio es requerido")
    private long precio;
    @NotNull(message = "La cantidad es requerida")
    private int cantidad;
    @NotNull(message = "El id de la persona es requerido")
    private int idPersona;
    @NotNull(message = "la fecha es requerida")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate fecha;
    @NotNull(message = "El id de la categoria es requerido")
    private int idCategoria;

    public  String getNombre() {
        return nombre;
    }

    public void setNombre( String nombre) {
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

    public void setCantidad( int cantidad) {
        this.cantidad = cantidad;
    }


    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona( int idPersona) {
        this.idPersona = idPersona;
    }

    public  LocalDate getFecha() {
        return fecha;
    }

    public void setFecha( LocalDate fecha) {
        this.fecha = fecha;
    }


    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria( int idCategoria) {
        this.idCategoria = idCategoria;
    }
}
