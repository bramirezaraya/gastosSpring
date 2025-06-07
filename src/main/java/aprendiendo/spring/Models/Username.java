package aprendiendo.spring.Models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Username {

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email no es valido")
    public String email;
    @NotBlank(message = "La contraseña es requerida")
    public String password;

}
