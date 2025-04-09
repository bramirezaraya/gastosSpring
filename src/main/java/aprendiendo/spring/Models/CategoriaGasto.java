package aprendiendo.spring.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "categoria")
public class CategoriaGasto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "El nombre es requerido")
    private String nombre;

    public CategoriaGasto(String nombre) {
        this.nombre = nombre;
    }
    public CategoriaGasto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotNull(message = "El nombre es requerido") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotNull(message = "El nombre es requerido") String nombre) {
        this.nombre = nombre;
    }
}
