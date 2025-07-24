package aprendiendo.spring.Models;

import lombok.Data;
import lombok.Getter;

@Data
public class TokenValidationResult {
    private final boolean valido;
    private final String mensaje;
    private final String subject;

    public TokenValidationResult(boolean valido, String mensaje, String subject) {
        this.valido = valido;
        this.mensaje = mensaje;
        this.subject = subject;
    }

    public boolean isValido() { return valido; }
    public String getMensaje() { return mensaje; }
    public String getSubject() { return subject; }
}
