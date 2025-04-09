package aprendiendo.spring.ServicesImp;

import aprendiendo.spring.Exception.RequestException;
import aprendiendo.spring.Models.*;
import aprendiendo.spring.Repository.CategoriaRepository;
import aprendiendo.spring.Repository.GastosRepository;
import aprendiendo.spring.Repository.PersonaRepository;
import aprendiendo.spring.Services.ServicioGastos;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class servicioGastosImp implements ServicioGastos {

    @Autowired
    private GastosRepository repository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ObjectNode getGastos(LocalDate fechaInicio, LocalDate fechaFin, int idPersona, int pageNumber, int size) {
        Page<Gastos> listaGastos;
        List<Gastos> listaTotal = new ArrayList();
        Pageable pageable = PageRequest.of(pageNumber, size);
        Optional<Persona> persona = personaRepository.findById(idPersona);
        List<CategoriaGasto> categorias = categoriaRepository.findAll();
        double totalSum = 0;
        if(persona.isPresent()){
            Persona person = persona.get();
            if(fechaInicio == null && fechaFin == null ){
                listaGastos = repository.findByPersonaOrderByFecha(person, pageable);
                listaTotal = repository.findByPersona(person);
            } else {
                listaGastos = repository.findByFechaBetweenAndPersonaOrderByFecha(fechaInicio, fechaFin, person, pageable);
                listaTotal = repository.findByFechaBetweenAndPersona(fechaInicio, fechaFin, person);
            }
            totalSum = listaTotal.stream().mapToDouble(Gastos::getPrecio).sum();
        } else {
            listaGastos = Page.empty();
        }
        List<CategoriaTotal> totalPerCategory = getTotalPerCategory(listaTotal, categorias);
        ObjectNode response = objectMapper.createObjectNode();
        List<GastoDTO> gastosDto = new ArrayList<>();
        listaGastos.getContent().forEach( datos -> {
            GastoDTO gastoDTO = new GastoDTO();
            gastoDTO.setNombre(datos.getNombre());
            gastoDTO.setPrecio(datos.getPrecio());
            gastoDTO.setCantidad(datos.getCantidad());
            gastoDTO.setIdPersona(datos.getPersona().getId());
            gastoDTO.setFecha(datos.getFecha());
            gastoDTO.setIdCategoria(datos.getCategoriaGasto().getId());
            gastosDto.add(gastoDTO);
        });
        response.put("gastosPorCategorias", objectMapper.valueToTree(totalPerCategory));
        response.put("Gastos", objectMapper.valueToTree(gastosDto));
        response.put("pagina", listaGastos.getNumber());
        response.put("totalPaginas", listaGastos.getTotalPages());
        response.put("totalElementos", listaGastos.getTotalElements());
        response.put("message", "Gastos");
        response.put("totalGastado", totalSum);
        return response;
    }
    public ObjectNode getGasto(int id) {

        Optional<Gastos> gasto = repository.findById(id);
        ObjectNode response = objectMapper.createObjectNode();
        if(gasto.isPresent()){
            response.put("message", "Gasto encontrado");
            response.put("gasto", objectMapper.valueToTree(gasto.get()));
        } else{
            throw new RequestException("Gasto no existe", 401, false, HttpStatus.BAD_REQUEST);
        }
        return response;
    }
    public ObjectNode saveGasto(GastoDTO gastoDTO) {
        ObjectNode response = objectMapper.createObjectNode();
        Optional<Persona> persona = personaRepository.findById(gastoDTO.getIdPersona());
        Optional<CategoriaGasto> categoriaGasto = categoriaRepository.findById(gastoDTO.getIdCategoria());
            if(persona.isEmpty() || categoriaGasto.isEmpty()){
                response.put("message", "Persona o categoria no encontrada");
                throw new RequestException("Persona o categoria no encontrada", 401, false, HttpStatus.BAD_REQUEST);
            }
            Gastos gasto = new Gastos(gastoDTO.getNombre(), gastoDTO.getPrecio(), gastoDTO.getCantidad(), persona.get(), gastoDTO.getFecha(), categoriaGasto.get());
            repository.save(gasto);
            response.put("message", "Gasto guardado");
            response.put("gasto", objectMapper.valueToTree(gasto));
        return response;
    }
    public ObjectNode updateGasto(Gastos gasto) {
        return null;
    }
    public ObjectNode deleteGasto(int id) {
        repository.deleteById(id);
        ObjectNode response = objectMapper.createObjectNode();
        response.put("message", "Gasto eliminado");
        return response;
    }


    public List<CategoriaTotal> getTotalPerCategory(List<Gastos> listaTotal, List<CategoriaGasto> categorias) {
        Map<String, Double> totalPerCategory = new HashMap<>();
        for( CategoriaGasto categoria : categorias){
            double amount = 0;
            for (Gastos gasto : listaTotal) {
                String categoriass = gasto.getCategoriaGasto().getNombre();
                if(categoria.getNombre() == categoriass){
                    amount += gasto.getPrecio();
                }
            }
            totalPerCategory.put(categoria.getNombre(), amount);
        }
        List<CategoriaTotal> totalPerCategoryList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : totalPerCategory.entrySet()) {
            totalPerCategoryList.add(new CategoriaTotal(entry.getKey(), entry.getValue().longValue()));
        }

        return totalPerCategoryList;
    }
}
