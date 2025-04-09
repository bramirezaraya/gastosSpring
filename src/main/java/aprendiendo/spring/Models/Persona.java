package aprendiendo.spring.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

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
    @NotNull(message = "El sueldo es requerido")
    private Double sueldo;
    @NotNull(message = "El password es requerido")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private long metaAhorro;

//    @OneToMany(mappedBy = "idPersona", cascade = CascadeType.ALL)
//    @OrderBy("fecha ASC")
//    private List<Gastos> gastos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Double getSueldo() {
        return sueldo;
    }

    public void setSueldo(Double sueldo) {
        this.sueldo = sueldo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password) {
        this.password = password;
    }

    public long getMetaAhorro() {
        return metaAhorro;
    }

    public void setMetaAhorro(long metaAhorro) {
        this.metaAhorro = metaAhorro;
    }

    //    public List<Gastos> getGastos() {
//        return gastos;
//    }
//
//    public void setGastos(List<Gastos> gastos) {
//        this.gastos = gastos;
//    }
}
