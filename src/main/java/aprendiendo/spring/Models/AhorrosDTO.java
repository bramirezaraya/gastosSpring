package aprendiendo.spring.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

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

//    public AhorrosDTO(String nombre, double monto, LocalDate fecha, Integer id_persona) {
//        this.nombre = nombre;
//        this.monto = monto;
//        this.fecha = fecha;
//        this.id_persona = id_persona;
//    }

    public double getMonto() {
        return monto;
    }

    public void setMonto( double monto) {
        this.monto = monto;
    }

    public  LocalDate getFecha() {
        return fecha;
    }

    public void setFecha( LocalDate fecha) {
        this.fecha = fecha;
    }

    public  Integer getId_persona() {
        return id_persona;
    }

    public void setId_persona( Integer id_persona) {
        this.id_persona = id_persona;
    }

    public  String getNombre() {
        return nombre;
    }

    public void setNombre( String nombre) {
        this.nombre = nombre;
    }
}
