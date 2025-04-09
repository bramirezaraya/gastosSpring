package aprendiendo.spring.Services;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ServicioCategoria {
    ObjectNode getCategorias();
    ObjectNode getCategoria(int id);
    ObjectNode deteleCategoria(int id);
    ObjectNode updateCategoria(int id, String nombre);
    ObjectNode saveCategoria(String nombre);
}
