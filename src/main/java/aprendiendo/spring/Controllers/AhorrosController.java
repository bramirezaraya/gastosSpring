package aprendiendo.spring.Controllers;

import aprendiendo.spring.Models.AhorrosDTO;
import aprendiendo.spring.Models.ResponseSucess;
import aprendiendo.spring.Models.Status;
import aprendiendo.spring.Services.ServicioAhorro;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ahorros")
public class AhorrosController {

    @Autowired
    private ServicioAhorro servicioAhorro;

    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping("/all")
    public ResponseEntity getAhorros(
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fechaFin,
            @RequestParam int idPersona,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "5") int size) {
        ResponseSucess responseSucces = new ResponseSucess();
        if (fechaInicio == null && fechaFin == null) {
            fechaFin = LocalDate.now();
            fechaInicio = fechaFin.minusDays(30);
        }
        ObjectNode response = servicioAhorro.getAhorros(fechaInicio,fechaFin,idPersona,pageNumber,size);
        ObjectNode page = objectMapper.createObjectNode();
        page.put("totalPaginas", response.findValue("totalPaginas").asInt());
        page.put("Pagina", response.findValue("pagina").asInt());
        page.put("totalElementos", response.findValue("totalElementos").asInt());
        page.put("total", response.findValue("totalGastado").asDouble());
        page.put("Ahorros", response.findValue("Ahorros"));
        page.put("nombre", "Total ahorrado");
        responseSucces.setStatus(
                new Status(response.findValue("status").asInt(),
                        response.findValue("message").asText()
                ));
        responseSucces.setData(page);
        responseSucces.setResult(true);
        return ResponseEntity.ok().body(responseSucces);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseSucess> saveAhorros(@Valid @RequestBody AhorrosDTO ahorro, BindingResult result) {
        if(result.hasFieldErrors()){
            return validateError(result);
        }
        ResponseSucess responseSucces = new ResponseSucess();
        ObjectNode response = servicioAhorro.saveAhorro(ahorro);
        if(response.findValue("status").asInt() == 200){
            responseSucces.setStatus(
                    new Status(response.findValue("status").asInt(),
                            response.findValue("message").asText()
                    ));
            responseSucces.setResult(true);
            return ResponseEntity.ok().body(responseSucces);
        } else {
            responseSucces.setStatus(
                    new Status(response.findValue("status").asInt(),
                            response.findValue("message").asText()
                    ));
            responseSucces.setResult(false);
            return ResponseEntity.badRequest().body(responseSucces);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseSucess> deleteAhorros(@PathVariable int id) {
        ResponseSucess responseSucces = new ResponseSucess();
        ObjectNode response = servicioAhorro.deleteAhorro(id);

        if(response.findValue("status").asInt() == 200){
            responseSucces.setStatus(
                    new Status(response.findValue("status").asInt(),
                            response.findValue("message").asText()
                    ));
            responseSucces.setResult(true);
            return ResponseEntity.ok().body(responseSucces);
        } else {
            responseSucces.setStatus(
                    new Status(response.findValue("status").asInt(),
                            response.findValue("message").asText()
                    ));
            responseSucces.setResult(false);
            return ResponseEntity.badRequest().body(responseSucces);
        }
    }

    public ResponseEntity<ResponseSucess> validateError(BindingResult result) {
        List errores = new ArrayList();
        ResponseSucess response = new ResponseSucess();
        response.setResult(false);
        response.setStatus(new Status(400, "Error en los datos"));
        result.getFieldErrors().forEach(error -> {
            errores.add(error.getDefaultMessage());
        });
        response.setData(errores);
        return ResponseEntity.badRequest().body(response);
    }
}
