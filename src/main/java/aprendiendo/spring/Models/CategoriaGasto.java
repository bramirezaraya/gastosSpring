package aprendiendo.spring.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
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
}
