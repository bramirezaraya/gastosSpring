package aprendiendo.spring.Models;

public class CategoriaTotal {

    private String categoria;
    private long total;


    public CategoriaTotal() {
    }
    public CategoriaTotal(String categoria, long total) {
        this.categoria = categoria;
        this.total = total;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
