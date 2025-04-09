package aprendiendo.spring.Exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class RequestException extends RuntimeException{
    String message;
    int code;
    boolean status;
    HttpStatus httpStatus;

    public RequestException(String message, int code, boolean status, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.code = code;
        this.status = status;
        this.httpStatus = httpStatus;
    }
}
