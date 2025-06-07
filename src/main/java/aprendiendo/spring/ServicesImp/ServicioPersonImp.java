package aprendiendo.spring.ServicesImp;

import aprendiendo.spring.Exception.RequestException;
import aprendiendo.spring.Models.Ahorros;
import aprendiendo.spring.Models.Gastos;
import aprendiendo.spring.Models.Persona;
import aprendiendo.spring.Models.Username;
import aprendiendo.spring.Repository.AhorroRepository;
import aprendiendo.spring.Repository.GastosRepository;
import aprendiendo.spring.Repository.PersonaRepository;
import aprendiendo.spring.Services.ServicioPerson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioPersonImp implements ServicioPerson {

    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder password;
    @Autowired
    GastosRepository gastosRepository;
    @Autowired
    AhorroRepository ahorrosRepository;

    @Override
    public ObjectNode registerPerson(Persona person) {

        Optional<Persona> persona = personaRepository.findByEmail(person.getEmail());
        ObjectNode response = objectMapper.createObjectNode();
        if(persona.isPresent()){
            throw new RequestException("Email ya registrado", 401, false, HttpStatus.BAD_REQUEST);
        }
        String passwordEncode = password.encode(person.getPassword());
        //decodificar se usa password.matches(password, passwordEncoder)
        person.setPassword(passwordEncode);
        personaRepository.save(person);
        response.put("message", "Persona guardada correctamente");
        response.put("persona", objectMapper.valueToTree(person));
        return response;
    }

    @Override
    public ObjectNode getPerson(int id) {
        Optional<Persona> persona = personaRepository.findById(id);
        ObjectNode response = objectMapper.createObjectNode();

        if(persona.isPresent()){
            response.put("Persona", objectMapper.valueToTree(persona.get()));
            response.put("message", "Persona encontrada");
            response.put("status", 200);
        }else{
            throw new RequestException("Persona no encontrada", 401, false, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ObjectNode getPeople() {

        List<Persona> people = personaRepository.findAll();
        ObjectNode response = objectMapper.createObjectNode();
        response.put("Personas", objectMapper.valueToTree(people));
        response.put("status", 200);

        return response;
    }
    @Override
    public ObjectNode updatePerson(Persona person) {
        return null;
    }

    @Override
    public ObjectNode deletePerson(int id) {

        Optional<Persona> person = personaRepository.findById(id);
        ObjectNode response = objectMapper.createObjectNode();

        if(person.isPresent()){
            personaRepository.deleteById(id);
            response.put("status", 200);
            response.put("message", "Persona eliminada correctamente");
        } else {
            throw new RequestException("Persona no encontrada", 401, false, HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    @Override
    public ObjectNode loginPerson(Username user) {

        Optional<Persona> persona = personaRepository.findByEmail(user.getEmail());
        ObjectNode response = objectMapper.createObjectNode();
        if(persona.isPresent()) {
            if(password.matches(user.getPassword(), persona.get().getPassword())) {
                response.put("message", "Usuario logeado correctamente");
                response.put("status", 200);
                response.put("Persona", objectMapper.valueToTree(persona.get()));
            } else {
                throw new RequestException("Contraseña incorrecta", 404, false, HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new RequestException("Usuario no encontrada", 404, false, HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    @Override
    public ObjectNode infoPerson(int id, LocalDate fechaInicio, LocalDate fechaFin) {

        Optional<Persona> person = personaRepository.findById(id);
        double totalGastos= 0;
        double totalAhorros = 0;
        ObjectNode response = objectMapper.createObjectNode();
        if(!person.isPresent()){
            throw new RequestException("Persona no encontrada", 401, false, HttpStatus.BAD_REQUEST);
        }

        List<Ahorros> ahorros = ahorrosRepository.findByFechaBetweenAndPersona(fechaInicio, fechaFin, person.get());
        List<Gastos> gastos = gastosRepository.findByFechaBetweenAndPersona(fechaInicio, fechaFin, person.get());
        totalGastos = gastos.stream().mapToDouble(Gastos::getPrecio).sum();
        totalAhorros = ahorros.stream().mapToDouble(Ahorros::getMonto).sum();

        response.put("gastos", createInfoObject("Gastos", totalGastos));
        response.put("ahorros", createInfoObject("Ahorros", totalAhorros));
        response.put("presupuesto", createInfoObject("Presupuesto", person.get().getPresupuesto()));
        response.put("disponible", createInfoObject("Disponible", person.get().getPresupuesto() - (totalGastos + totalAhorros)));
        return response;
    }

    private ObjectNode createInfoObject(String name, double amount) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("nombre", name);
        node.put("monto", amount);
        return node;
    }
}
