package aprendiendo.spring.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email no es valido")
    private String email;
    @NotBlank(message = "El nombre no puede contener espacios")
    @NotNull(message = "El nombre es requerido")
    private String nombre;
    @NotBlank(message = "El apellido es requerido")
    private String apellido;
    @NotNull(message = "El presupuesto es requerido")
    private Double presupuesto;
    @NotNull(message = "El password es requerido")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private long metaAhorro;

//    @OneToMany(mappedBy = "idPersona", cascade = CascadeType.ALL)
//    @OrderBy("fecha ASC")
//    private List<Gastos> gastos;

}
