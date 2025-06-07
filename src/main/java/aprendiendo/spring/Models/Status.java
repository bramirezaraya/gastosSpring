package aprendiendo.spring.Models;

import lombok.Data;

@Data
public class Status {

    public int codigo;
    public String mensaje;

    public Status(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

}
