package aprendiendo.spring.Controllers;

import aprendiendo.spring.Models.PrestadoDTO;
import aprendiendo.spring.Models.ResponseSucess;
import aprendiendo.spring.Models.Status;
import aprendiendo.spring.Services.ServicioPrestado;
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
@RequestMapping("/prestamo")
public class PrestamoController {

    @Autowired
    private ServicioPrestado prestamoService;
    @Autowired
    public ObjectMapper objectMapper;


    @GetMapping("/all")
    public ResponseEntity<ResponseSucess> getAllPrestamo(
            @RequestParam() int idPersona,
            @RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fechaInicio,
            @RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fechaFin,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size
    ){
        ResponseSucess response = new ResponseSucess();
        ObjectNode result = prestamoService.getPrestados(idPersona, fechaInicio, fechaFin, page, size);
        ObjectNode resultData = objectMapper.createObjectNode();
        resultData.put("Data", result.findValue("Prestamos"));
        resultData.put("pagina", result.findValue("pagina"));
        resultData.put("totalPaginas", result.findValue("totalPaginas"));
        resultData.put("totalElementos", result.findValue("totalElementos"));
        resultData.put("DineroTotal", result.findValue("DineroTotal"));
        response.setData(resultData);
        response.setResult(true);
        response.setStatus(new Status(result.findValue("status").asInt(), result.findValue("Message").asText()));
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseSucess> savePrestamo(@Valid @RequestBody PrestadoDTO body, BindingResult result) {
        if(result.hasFieldErrors()){
            return validateError(result);
        }
        ResponseSucess response = new ResponseSucess();
        ObjectNode resultData = prestamoService.savePrestado(body);
        response.setData(result);
        response.setResult(true);
        response.setStatus(new Status(resultData.findValue("status").asInt(), resultData.findValue("Message").asText()));
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ResponseSucess> deletePrestamo(@PathVariable int id) {
        ResponseSucess response = new ResponseSucess();
        ObjectNode result = prestamoService.deletePrestado(id);
        response.setData(result);
        response.setResult(true);
        response.setStatus(new Status(result.findValue("status").asInt(), result.findValue("Message").asText()));
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<ResponseSucess> validateError(BindingResult result) {
        ResponseSucess response = new ResponseSucess();
        List errores = new ArrayList();
        response.setStatus(new Status(400, "Error en la validación de los datos"));
        response.setResult(false);
        result.getFieldErrors().forEach( error -> {
            errores.add(error.getDefaultMessage());
        });
        response.setData(errores);
        return ResponseEntity.badRequest().body(response);
    }

}
