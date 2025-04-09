package aprendiendo.spring.Controllers;

import aprendiendo.spring.Models.ResponseSucess;
import aprendiendo.spring.Models.Status;
import aprendiendo.spring.Services.ServicioCategoria;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ServicioCategoria servicioCategoria;
    @GetMapping("/all")
    public ResponseEntity<ResponseSucess> getCategorias(){

        ObjectNode response = objectMapper.createObjectNode();
        response = servicioCategoria.getCategorias();

        ResponseSucess responseSucess = new ResponseSucess();
        responseSucess.setData(response.findValue("categorias"));
        responseSucess.setResult(true);
        responseSucess.setStatus( new Status(response.findValue("status").asInt(), response.findValue("message").asText()));

        return ResponseEntity.ok().body(responseSucess);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseSucess> saveCategoria(String nombre){
        ObjectNode response = servicioCategoria.saveCategoria(nombre);

        ResponseSucess responseSucess = new ResponseSucess();
        responseSucess.setData(response.findValue("categoria"));
        responseSucess.setResult(true);
        responseSucess.setStatus( new Status(response.findValue("status").asInt(), response.findValue("message").asText()));

        return ResponseEntity.ok().body(responseSucess);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseSucess> deleteCategoria(@PathVariable int idCategoria){
        ResponseSucess responseSucces = new ResponseSucess();
        ObjectNode response = servicioCategoria.deteleCategoria(idCategoria);
        responseSucces.setStatus(new Status(response.findValue("status").asInt(), response.findValue("message").asText()));
        if(response.findValue("status").asInt() == 200){
            responseSucces.setResult(true);
            return ResponseEntity.ok().body(responseSucces);
        } else {
            responseSucces.setResult(false);
            return ResponseEntity.badRequest().body(responseSucces);
        }
    }

}
