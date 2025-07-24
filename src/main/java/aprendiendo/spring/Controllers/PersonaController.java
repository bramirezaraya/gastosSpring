package aprendiendo.spring.Controllers;

import aprendiendo.spring.Models.*;
import aprendiendo.spring.Services.ServicioPerson;
import aprendiendo.spring.util.JwtService;
import com.fasterxml.jackson.databind.JsonNode;
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
@RequestMapping("/auth")
public class PersonaController {

    @Autowired
    private ServicioPerson servicioPerson;
    @Autowired
    JwtService jwtService;

    @GetMapping("/all")
    public ResponseEntity<ResponseSucess> findPeople(){

        ObjectNode response = servicioPerson.getPeople();

        ResponseSucess responseSucess = new ResponseSucess();
        responseSucess.setStatus(new Status(200, "Respuesta exitosa"));
        responseSucess.setResult(true);
        responseSucess.setData(response.findValue("Personas"));
        return ResponseEntity.ok().body(responseSucess);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseSucess> registerPerson(@Valid @RequestBody Persona body, BindingResult result){

        if (result.hasFieldErrors()) {
            return validateError(result);
        }
        ObjectNode response = servicioPerson.registerPerson(body);
        ResponseSucess responseSucess = new ResponseSucess();
        responseSucess.setStatus(new Status(200, response.findValue("message").asText()));
        responseSucess.setResult(true);
        responseSucess.setData(response.findValue("persona"));
        return ResponseEntity.ok().body(responseSucess);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseSucess> loginPerson (@Valid @RequestBody Username user) {
        ResponseSucess response = new ResponseSucess();
        ObjectNode username = servicioPerson.loginPerson(user);
        response.setResult(true);
        response.setStatus(new Status(200, username.findValue("message").asText()));
        response.setData(username.findValue("Persona"));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<ResponseSucess> getPerson(@PathVariable int id){
        ObjectNode response = servicioPerson.getPerson(id);
        ResponseSucess responseSucess = new ResponseSucess();
        if(response.findValue("status").asInt() == 404){
            responseSucess.setStatus(new Status(404, response.findValue("message").asText()));
            responseSucess.setResult(false);
            return ResponseEntity.badRequest().body(responseSucess);
        }
        responseSucess.setStatus(new Status(response.findValue("status").asInt(), response.findValue("message").asText()));
        responseSucess.setResult(true);
        responseSucess.setData(response.findValue("Persona"));
        return ResponseEntity.ok().body(responseSucess);
    }

    @GetMapping("/person/info")
    public ResponseEntity<ResponseSucess> getInfoPerson(@RequestParam(required = false, defaultValue = "")
                                                            @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fechaInicio,
                                                        @RequestParam(required = false, defaultValue = "")
                                                            @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fechaFin,
                                                        @RequestParam int idPersona) {
        if(fechaInicio == null && fechaFin == null) {
            fechaFin = LocalDate.now();
            fechaInicio = fechaFin.minusDays(30);
        }

        ObjectNode response = servicioPerson.infoPerson(idPersona, fechaInicio, fechaFin);

        ResponseSucess responseSucess = new ResponseSucess();
        responseSucess.setData(response);
        responseSucess.setStatus(new Status(200, "Consulta exitosa"));
        responseSucess.setResult(true);
        return ResponseEntity.ok().body(responseSucess);

    }
    @PostMapping("/verifyToken")
    public ResponseEntity<ResponseSucess> verifyToken(@RequestBody JsonNode dataToken) {
        ResponseSucess responseSucess = new ResponseSucess();
        if (dataToken.findValue("token").asText() == null ||  dataToken.findValue("token").asText().isEmpty()) {
            responseSucess.setStatus(new Status(401, "Token no proporcionado"));
            responseSucess.setResult(false);
            return ResponseEntity.status(401).body(responseSucess);
        }
        TokenValidationResult tokenVerificado = jwtService.ValidateToken(dataToken.findValue("token").asText());
        if(!tokenVerificado.isValido()) {
            responseSucess.setStatus(new Status(401, tokenVerificado.getMensaje()));
            responseSucess.setResult(false);
            return ResponseEntity.status(401).body(responseSucess);
        }
        responseSucess.setStatus(new Status(200, "Token válido"));
        responseSucess.setResult(true);
        return ResponseEntity.ok().body(responseSucess);
    }

    public ResponseEntity<ResponseSucess> validateError(BindingResult result){
        List errores = new ArrayList();
        ResponseSucess responseSucess = new ResponseSucess();
        responseSucess.setStatus(new Status(400, "Error en los datos"));
        responseSucess.setResult(false);
        result.getFieldErrors().forEach(error -> {
            errores.add(error.getDefaultMessage());
        });
        responseSucess.setData(errores);
        return ResponseEntity.badRequest().body(responseSucess);
    }

}
