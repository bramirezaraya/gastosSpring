package aprendiendo.spring.Controllers;

import aprendiendo.spring.Exception.RequestException;
import aprendiendo.spring.Models.GastoDTO;
import aprendiendo.spring.Models.Gastos;
import aprendiendo.spring.Models.ResponseSucess;
import aprendiendo.spring.Models.Status;
import aprendiendo.spring.Services.ServicioGastos;
import aprendiendo.spring.util.LogRegister;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private LogRegister logRegister = new LogRegister();
//    private static final Logger LOG = LoggerFactory.getLogger(GastosController.class);

    @GetMapping("/all")
    public ResponseEntity<ResponseSucess> getGastos(@RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fechaInicio,
                                                    @RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fechaFin,
                                                    @RequestParam int idPersona,
                                                    @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size){
        ResponseSucess responseSucess = new ResponseSucess();
        ObjectNode entradaServicio = objectMapper.createObjectNode();
        entradaServicio.put("fechaInicio", fechaInicio != null ? fechaInicio.toString() : "");
        entradaServicio.put("fechaFin", fechaFin != null ? fechaFin.toString() : "");
        entradaServicio.put("idPersona", idPersona);
        entradaServicio.put("pageNumber", pageNumber);
        entradaServicio.put("size", size);
        logRegister.entradaController(entradaServicio, "/gastos/all");
        if (fechaInicio == null && fechaFin == null) {
            fechaFin = LocalDate.now();
            fechaInicio = fechaFin.minusDays(30);
        }
        ObjectNode response = servicio.getGastos(fechaInicio, fechaFin, idPersona, pageNumber, size);
        ObjectNode responseFinal = objectMapper.createObjectNode();
        responseFinal.put("Gastos", response.findValue("Gastos"));
        responseFinal.put("pagina", response.findValue("pagina"));
        responseFinal.put("totalPaginas", response.findValue("totalPaginas"));
        responseFinal.put("totalElementos", response.findValue("totalElementos"));
        responseSucess.setData(responseFinal);
        responseSucess.setStatus( new Status(200, "Gastos completado"));
        responseSucess.setResult(true);
        return ResponseEntity.ok().body(responseSucess);
    }

    @GetMapping("/ultimosGastos")
    public ResponseEntity<ResponseSucess> ultimasCompras(@RequestParam int idPersona) {
        ObjectNode entradaServicio = objectMapper.createObjectNode();
        entradaServicio.put("idPersona", idPersona);
        logRegister.entradaController(entradaServicio, "/gastos/ultimosGastos");
        ResponseSucess responseSucess = new ResponseSucess();
        ObjectNode response = servicio.ultimosGastos(idPersona);

        responseSucess.setData(response);
        responseSucess.setResult(true);
        responseSucess.setStatus(new Status(200, "Ultimos gastos"));
        return ResponseEntity.ok().body(responseSucess);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseSucess> saveGasto(@RequestBody @Valid GastoDTO body){
        ObjectNode entradaServicio = objectMapper.createObjectNode();
        entradaServicio.put("body", body.toString());
        logRegister.entradaController(entradaServicio, "/gastos/save");
        ResponseSucess responseSucess = new ResponseSucess();
        ObjectNode response = servicio.saveGasto(body);
        responseSucess.setData(response.findValue("gasto"));
        responseSucess.setStatus( new Status(200, response.findValue("message").asText()));
        responseSucess.setResult(true);
        return ResponseEntity.ok().body(responseSucess);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseSucess> deleteGasto (@PathVariable int idGasto) {
        ObjectNode entradaServicio = objectMapper.createObjectNode();
        entradaServicio.put("idGasto", idGasto);
        logRegister.entradaController(entradaServicio, String.format("/gastos/delete/%d", idGasto));
        ResponseSucess responseSucess = new ResponseSucess();
        ObjectNode response = servicio.deleteGasto(idGasto);
        responseSucess.setStatus(new Status(200, response.findValue("message").asText()));
        responseSucess.setResult(true);
        return ResponseEntity.ok().body(responseSucess);
    }

    @GetMapping("/gastosPorCategoria")
    public ResponseEntity<ResponseSucess> gastosPorCategoria (
            @RequestParam int idPersona,
            @RequestParam (required = false, defaultValue = "") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fechaInicio,
            @RequestParam (required = false, defaultValue = "") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fechaFin) {
        ResponseSucess responseSucess = new ResponseSucess();
        ObjectNode entradaServicio = objectMapper.createObjectNode();
        entradaServicio.put("fechaInicio", fechaInicio != null ? fechaInicio.toString() : "");
        entradaServicio.put("fechaFin", fechaFin != null ? fechaFin.toString() : "");
        entradaServicio.put("idPersona", idPersona);
        logRegister.entradaController(entradaServicio, "/gastos/gastosPorCategoria");
        if(fechaInicio == null && fechaFin == null){
            fechaFin = LocalDate.now();
            fechaInicio = fechaFin.minusDays(30);
        }

        ObjectNode response = servicio.gastosPorCategoria(idPersona, fechaInicio, fechaFin);
        responseSucess.setData(response);
        responseSucess.setStatus( new Status(200, "Gastos por categorias"));
        responseSucess.setResult(true);
        return ResponseEntity.ok().body(responseSucess);
    }


    @GetMapping("/gastosAhorrosPorMes")
    public ResponseEntity<ResponseSucess> gastosAhorrosPorMes(@RequestParam int idPersona, @RequestParam int year) {
        ObjectNode entradaServicio = objectMapper.createObjectNode();
        entradaServicio.put("year",year);
        entradaServicio.put("idPersona", idPersona);
        logRegister.entradaController(entradaServicio, "/gastos/gastosAhorrosPorMes");
        ObjectNode response = servicio.gastosAhorrosPorMes(idPersona, year);
        ResponseSucess responseSucess = new ResponseSucess();
        responseSucess.setData(response);
        responseSucess.setStatus( new Status(200, "info por mes"));
        responseSucess.setResult(true);
        return ResponseEntity.ok().body(responseSucess);
    }
    public ResponseEntity<ResponseSucess> validateError(BindingResult result){
        List errores = new ArrayList();
        result.getFieldErrors().forEach(error -> {
            errores.add(error.getDefaultMessage());
        });
        throw new RequestException(errores.toString(), 400, false, HttpStatus.BAD_REQUEST);
    }



}
