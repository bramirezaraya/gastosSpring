package aprendiendo.spring.Models;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class ExeptionAdvice {

    String message;
    int code;
    boolean status;
}
