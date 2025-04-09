package aprendiendo.spring.ServicesImp;

import aprendiendo.spring.Exception.RequestException;
import aprendiendo.spring.Models.Persona;
import aprendiendo.spring.Models.Prestado;
import aprendiendo.spring.Models.PrestadoDTO;
import aprendiendo.spring.Repository.PersonaRepository;
import aprendiendo.spring.Repository.PrestadoRepository;
import aprendiendo.spring.Services.ServicioPrestado;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioPrestadoImp implements ServicioPrestado {

    @Autowired
    private PrestadoRepository prestadoRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ObjectNode getPrestados(int idPersona, LocalDate fechaInicio, LocalDate fechaFin, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Prestado> prestado;
        ObjectNode response = objectMapper.createObjectNode();
        Optional<Persona> person = personaRepository.findById(idPersona);
        if(person.isPresent()){
            Persona persona = person.get();
            if(fechaInicio == null && fechaFin == null) {
                prestado = prestadoRepository.findByPersonaOrderByFecha(persona, pageable);
            } else {
                prestado = prestadoRepository.findByFechaBetweenAndPersonaOrderByFecha(fechaInicio, fechaFin, persona, pageable);
            }
            response.put("Message", "lista de prestamos");
            response.put("status", 200);
        } else {
            prestado = Page.empty();
            response.put("Message", "No tienes ningun prestamos.");
            response.put("status", 202);
        }
        List<PrestadoDTO> listadoPrestamo = new ArrayList<>();
        final double[] dineroTotal = {0};
        prestado.getContent().forEach( data -> {
            PrestadoDTO prestadoDTO = new PrestadoDTO();
            prestadoDTO.setNombre(data.getNombre());
            prestadoDTO.setDinero(data.getDinero());
            prestadoDTO.setFecha(data.getFecha());
            prestadoDTO.setDescripcion(data.getDescripcion());
            prestadoDTO.setIdPersona(data.getPersona().getId());
            dineroTotal[0] += data.getDinero();
            listadoPrestamo.add(prestadoDTO);
        });
        response.put("Prestamos", objectMapper.valueToTree(listadoPrestamo));
        response.put("pagina", prestado.getNumber());
        response.put("totalPaginas", prestado.getTotalPages());
        response.put("totalElementos", prestado.getTotalElements());
        response.put("DineroTotal", dineroTotal[0]);
        return response;
    }
    @Override
    public ObjectNode deletePrestado(int idPrestamo) {
        ObjectNode response = objectMapper.createObjectNode();
        Optional<Prestado> prestado = prestadoRepository.findById(idPrestamo);
        if(prestado.isPresent()) {
            prestadoRepository.deleteById(idPrestamo);
            response.put("Message", "Prestamo eliminado.");
            response.put("status", 200);
        } else {
            throw new RequestException("Prestamo no encontrada", 401, false, HttpStatus.BAD_REQUEST);
        }
        return response;
    }
    @Override
    public ObjectNode savePrestado(PrestadoDTO prestado) {
        ObjectNode response = objectMapper.createObjectNode();
        Optional<Persona> person = personaRepository.findById(prestado.getIdPersona());

        if(person.isPresent()) {
            Prestado prestados = new Prestado(
                prestado.getNombre(),
                prestado.getDinero(),
                prestado.getFecha(),
                person.get(),
                prestado.getDescripcion()
            );
            prestadoRepository.save(prestados);
            response.put("Message", "Prestamo guardado.");
            response.put("status", 200);
        } else {
            throw new RequestException("Persona no encontrada", 401, false, HttpStatus.BAD_REQUEST);
        }

        return response;
    }

}
