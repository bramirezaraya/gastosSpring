package aprendiendo.spring.Controllers;

import aprendiendo.spring.Exception.RequestException;
import aprendiendo.spring.Models.GastoDTO;
import aprendiendo.spring.Models.Gastos;
import aprendiendo.spring.Models.ResponseSucess;
import aprendiendo.spring.Models.Status;
import aprendiendo.spring.Services.ServicioGastos;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/gastos")
public class GastosController {
    @Autowired
    private ServicioGastos servicio;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/all")
    public ResponseEntity<ResponseSucess> getGastos(@RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fechaInicio,
                                                    @RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fechaFin,
                                                    @RequestParam int idPersona,
                                                    @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                    @RequestParam(required = false, defaultValue = "5") Integer size){
        ResponseSucess responseSucess = new ResponseSucess();
        if (fechaInicio == null && fechaFin == null) {
            fechaFin = LocalDate.now();
            fechaInicio = fechaFin.minusDays(30);
        }
        ObjectNode response = servicio.getGastos(fechaInicio, fechaFin, idPersona, pageNumber, size);
        ObjectNode responseFinal = objectMapper.createObjectNode();
        responseFinal.put("Gastos", response.findValue("Gastos"));
        responseFinal.put("total", response.findValue("totalGastado").asInt());
        responseFinal.put("gastosPorCategoria", response.findValue("gastosPorCategorias"));
        responseFinal.put("pagina", response.findValue("pagina"));
        responseFinal.put("totalPaginas", response.findValue("totalPaginas"));
        responseFinal.put("totalElementos", response.findValue("totalElementos"));
        responseFinal.put("nombre", "Total gastado");
        responseSucess.setData(responseFinal);
        responseSucess.setStatus( new Status(200, response.findValue("message").asText()));
        responseSucess.setResult(true);
        return ResponseEntity.ok().body(responseSucess);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseSucess> saveGasto(@RequestBody @Valid GastoDTO body){
        ResponseSucess responseSucess = new ResponseSucess();
//        if(result.hasFieldErrors()){
//            validateError(result);
//        }
        ObjectNode response = servicio.saveGasto(body);
        responseSucess.setData(response.findValue("gasto"));
        responseSucess.setStatus( new Status(200, response.findValue("message").asText()));
        responseSucess.setResult(true);
        return ResponseEntity.ok().body(responseSucess);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseSucess> deleteGasto (@PathVariable int idGasto) {
        ResponseSucess responseSucess = new ResponseSucess();
        ObjectNode response = servicio.deleteGasto(idGasto);
        responseSucess.setStatus(new Status(200, response.findValue("message").asText()));
        responseSucess.setResult(true);
        return ResponseEntity.ok().body(responseSucess);
    }

    public ResponseEntity<ResponseSucess> validateError(BindingResult result){
        List errores = new ArrayList();
//        ResponseSucess responseSucess = new ResponseSucess();
//        responseSucess.setStatus(new Status(400, "Error en los datos"));
//        responseSucess.setResult(false);
        result.getFieldErrors().forEach(error -> {
            errores.add(error.getDefaultMessage());
        });
//        responseSucess.setData(errores);
        throw new RequestException(errores.toString(), 400, false, HttpStatus.BAD_REQUEST);
    }



}
