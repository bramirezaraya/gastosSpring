package aprendiendo.spring.Models;

import lombok.Data;

@Data
public class CategoriaTotal {

    private String categoria;
    private long total;


    public CategoriaTotal() {
    }
    public CategoriaTotal(String categoria, long total) {
        this.categoria = categoria;
        this.total = total;
    }

}
