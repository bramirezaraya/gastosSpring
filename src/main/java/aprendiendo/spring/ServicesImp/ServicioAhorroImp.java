package aprendiendo.spring.ServicesImp;

import aprendiendo.spring.Exception.RequestException;
import aprendiendo.spring.Models.Ahorros;
import aprendiendo.spring.Models.AhorrosDTO;
import aprendiendo.spring.Models.Persona;
import aprendiendo.spring.Repository.AhorroRepository;
import aprendiendo.spring.Repository.PersonaRepository;
import aprendiendo.spring.Services.ServicioAhorro;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioAhorroImp implements ServicioAhorro {

    @Autowired
    private AhorroRepository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PersonaRepository personaRepository;

    @Override
    public ObjectNode getAhorros(LocalDate fechaInicio, LocalDate fechaFin, int idPersona, int pageNumber, int size) {
        ObjectNode response = objectMapper.createObjectNode();
        Optional<Persona> person = personaRepository.findById(idPersona);
        Pageable page = PageRequest.of(pageNumber, size);
        List<AhorrosDTO> ahorrosDTOS = new ArrayList<>();
        List<Ahorros> totalAhorros = new ArrayList<>();
        double totalSum = 0;
        Page<Ahorros> ahorros;
        if( person.isPresent() ){
            Persona persona = person.get();
            if( fechaInicio == null && fechaFin == null ) {
                ahorros = repository.findByPersonaOrderByFecha(persona, page );
                totalAhorros = repository.findByPersona(persona);
            } else {
                ahorros = repository.findByFechaBetweenAndPersonaOrderByFecha( fechaInicio, fechaFin, persona, page);
                totalAhorros = repository.findByFechaBetweenAndPersona(fechaInicio, fechaFin, persona);
            }
            totalSum = totalAhorros.stream().mapToDouble(Ahorros::getMonto).sum();
            response.put("status", 200);
        } else {
            response.put("status", 404);
            ahorros = Page.empty();
        }
        ahorros.getContent().forEach( datos -> {
            AhorrosDTO ahorro = new AhorrosDTO();
            ahorro.setNombre(datos.getNombre());
            ahorro.setFecha(datos.getFecha());
            ahorro.setMonto(datos.getMonto());
            ahorro.setId_persona(datos.getPersona().getId());
            ahorrosDTOS.add(ahorro);
        });
        response.put("totalGastado", totalSum);
        response.put("Ahorros", objectMapper.valueToTree(ahorrosDTOS));
        response.put("pagina", ahorros.getNumber());
        response.put("totalPaginas", ahorros.getTotalPages());
        response.put("totalElementos", ahorros.getTotalElements());
        response.put("message", response.findValue("status").asInt() == 404 ? "Persona no tiene ahorros ingresados." : "Lista de ahorros");
        return response;
    }

    @Override
    public ObjectNode getAhorro(int id) {
        ObjectNode response = objectMapper.createObjectNode();
        if (repository.findById(id).isPresent()) {
            response.put("ahorro", objectMapper.valueToTree(repository.findById(id).get()));
            response.put("message", "Ahorro encontrado");
            response.put("status", 200);
        } else {
            throw new RequestException("Ahorro no encontrado", 401, false, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ObjectNode saveAhorro(AhorrosDTO ahorros) {
        ObjectNode response = objectMapper.createObjectNode();
        Optional<Persona> persona = personaRepository.findById(ahorros.getId_persona());

        if (persona.isPresent()) {
            Ahorros ahorro = new Ahorros(ahorros.getNombre(), ahorros.getMonto(), ahorros.getFecha(), persona.get());
            repository.save(ahorro);
            response.put("message", "Ahorro guardado");
            response.put("status", 200);
        } else {
            throw new RequestException("Persona no encontrada", 401, false, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ObjectNode updateAhorro(int id, AhorrosDTO ahorros) {
        ObjectNode response = objectMapper.createObjectNode();
        Optional<Ahorros> ahorro = repository.findById(id);
        Optional<Persona> persona = personaRepository.findById(ahorros.getId_persona());

        if (ahorro.isPresent()) {
            if (persona.isPresent()) {
                ahorro.get().setNombre(ahorros.getNombre());
                ahorro.get().setMonto(ahorros.getMonto());
                ahorro.get().setFecha(ahorros.getFecha());
                ahorro.get().setPersona(persona.get());
                repository.save(ahorro.get());
                response.put("message", "Ahorro actualizado");
                response.put("status", 200);
                response.put("Ahorro", objectMapper.valueToTree(ahorro.get()));
            } else {
                response.put("message", "Persona no encontrada");
                response.put("status", 404);
            }
        } else {
            throw new RequestException("Ahorro no encontrada", 401, false, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ObjectNode deleteAhorro(int id){
        ObjectNode response = objectMapper.createObjectNode();
        if(repository.findById(id).isPresent()){
            repository.deleteById(id);
            response.put("message", "Ahorro eliminado");
            response.put("status", 200);
        } else {
            throw new RequestException("Ahorro no encontrada", 401, false, HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
