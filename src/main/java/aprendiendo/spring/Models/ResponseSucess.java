package aprendiendo.spring.Models;

import lombok.Data;

@Data
public class ResponseSucess {

    public Status status;
    public boolean result;
    public Object data;
}
