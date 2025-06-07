package aprendiendo.spring.Exception;

import aprendiendo.spring.Models.ExeptionAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvice{

    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<ExeptionAdvice> HandlerException(RequestException e) {
        ExeptionAdvice exception = ExeptionAdvice.builder()
                .message(e.getMessage())
                .status(e.status)
                .code(e.getCode())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(exception);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ExeptionAdvice> handlerMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<String> errores = new ArrayList<>();
        e.getFieldErrors().forEach(error -> {
            errores.add(error.getDefaultMessage());
        });
        ExeptionAdvice exception = ExeptionAdvice.builder()
                .message(errores.toString())
                .status(false)
                .code(400)
                .build();
        return ResponseEntity.status(400).body(exception);
    }
}
