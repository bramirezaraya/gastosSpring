package aprendiendo.spring.Models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class Username {

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email no es valido")
    public String email;
    @NotBlank(message = "La contraseña es requerida")
    public String password;


    public  String getEmail() {
        return email;
    }

    public void setEmail ( String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password) {
        this.password = password;
    }
}
