package aprendiendo.spring.ServicesImp;

import aprendiendo.spring.Exception.RequestException;
import aprendiendo.spring.Models.CategoriaGasto;
import aprendiendo.spring.Repository.CategoriaRepository;
import aprendiendo.spring.Services.ServicioCategoria;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioCategoriaImp implements ServicioCategoria {

    @Autowired
    private CategoriaRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ObjectNode getCategorias() {
       ObjectNode response = objectMapper.createObjectNode();

       List<CategoriaGasto> categoria = repository.findAll();
       response.put("categorias", objectMapper.valueToTree(categoria));
       response.put("message", "Lista de categorias");
       response.put("status", 200);
       return response;
    }

    @Override
    public ObjectNode getCategoria(int id) {
        Optional<CategoriaGasto> categoria = repository.findById(id);
        ObjectNode response = objectMapper.createObjectNode();
        if(categoria.isPresent()){
            response.put("categoria", objectMapper.valueToTree(categoria.get()));
            response.put("message", "Categoria encontrada");
            response.put("status", 200);
        } else {
            response.put("message", "Categoria no encontrada");
            response.put("status", 404);
        }
        return response;
    }

    @Override
    public ObjectNode deteleCategoria(int id) {
        Optional<CategoriaGasto> categoria = repository.findById(id);
        ObjectNode response = objectMapper.createObjectNode();
        if(categoria.isPresent()){
            repository.deleteById(id);
            response.put("message", "Categoria eliminada");
            response.put("status", 200);
        } else {
            response.put("message", "Categoria no encontrada");
            response.put("status", 404);
        }
        return response;
    }

    @Override
    public ObjectNode updateCategoria(int id, String nombre) {
        return null;
    }

    @Override
    public ObjectNode saveCategoria(String nombre) {
        ObjectNode response = objectMapper.createObjectNode();
        if(repository.existsByNombre(nombre)){
            throw new RequestException("La categoria ya existe", 400, false, HttpStatus.BAD_REQUEST);
        } else {
            CategoriaGasto categoria = new CategoriaGasto(nombre);
            repository.save(categoria);
            response.put("status",200);
            response.put("message", "La categoria se ha guardado");
        }
        return response;
    }
}
