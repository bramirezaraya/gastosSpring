package aprendiendo.spring.ServicesImp;

import aprendiendo.spring.Exception.RequestException;
import aprendiendo.spring.Models.*;
import aprendiendo.spring.Repository.AhorroRepository;
import aprendiendo.spring.Repository.CategoriaRepository;
import aprendiendo.spring.Repository.GastosRepository;
import aprendiendo.spring.Repository.PersonaRepository;
import aprendiendo.spring.Services.ServicioGastos;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.cglib.core.Local;
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
    @Autowired
    private AhorroRepository ahorroRepository;

    @Override
    public ObjectNode getGastos(LocalDate fechaInicio, LocalDate fechaFin, int idPersona, int pageNumber, int size) {
        Page<Gastos> listaGastos;
//        List<Gastos> listaTotal = new ArrayList();
        Pageable pageable = PageRequest.of(pageNumber, size);
        Optional<Persona> persona = personaRepository.findById(idPersona);
//        List<CategoriaGasto> categorias = categoriaRepository.findAll();
//        double totalSum = 0;
        if(persona.isPresent()){
            Persona person = persona.get();
            if(fechaInicio == null && fechaFin == null ){
                listaGastos = repository.findByPersonaOrderByFecha(person, pageable);
//                listaTotal = repository.findByPersona(person);
            } else {
                listaGastos = repository.findByFechaBetweenAndPersonaOrderByFecha(fechaInicio, fechaFin, person, pageable);
//                listaTotal = repository.findByFechaBetweenAndPersona(fechaInicio, fechaFin, person);
            }
//            totalSum = listaTotal.stream().mapToDouble(Gastos::getPrecio).sum();
        } else {
            listaGastos = Page.empty();
        }
//        List<CategoriaTotal> totalPerCategory = getTotalPerCategory(listaTotal, categorias);
        ObjectNode response = objectMapper.createObjectNode();
        List<GastoDTO> gastosDto = new ArrayList<>();
        listaGastos.getContent().forEach( datos -> {
            GastoDTO gastoDTO = new GastoDTO();
            gastoDTO.setNombre(datos.getNombre());
            gastoDTO.setPrecio(datos.getPrecio());
            gastoDTO.setCuotas(datos.getCuotas());
            gastoDTO.setIdPersona(datos.getPersona().getId());
            gastoDTO.setFecha(datos.getFecha());
            gastoDTO.setIdCategoria(datos.getCategoriaGasto().getId());
            gastosDto.add(gastoDTO);
        });
        response.put("Gastos", objectMapper.valueToTree(gastosDto));
        response.put("pagina", listaGastos.getNumber());
        response.put("totalPaginas", listaGastos.getTotalPages());
        response.put("totalElementos", listaGastos.getTotalElements());
        return response;
    }

    @Override
    public ObjectNode ultimosGastos(int idPersona) {
        ObjectNode response = objectMapper.createObjectNode();
        Pageable pageable = PageRequest.of(0, 10);
        Optional<Persona> persona = personaRepository.findById(idPersona);
        Page<Gastos> listaGastos = Page.empty();
        if(!persona.isPresent()) {
            throw new RequestException("Persona no existe", 401, false, HttpStatus.BAD_REQUEST);
        }
        Persona person = persona.get();
        listaGastos = repository.findByPersonaOrderByFechaDesc(person, pageable);
        List<GastoDTO> gastosDto = new ArrayList<>();
        listaGastos.getContent().forEach( datos -> {
            GastoDTO gastoDTO = new GastoDTO();
            gastoDTO.setNombre(datos.getNombre());
            gastoDTO.setPrecio(datos.getPrecio());
            gastoDTO.setCuotas(datos.getCuotas());
            gastoDTO.setIdPersona(datos.getPersona().getId());
            gastoDTO.setFecha(datos.getFecha());
            gastoDTO.setIdCategoria(datos.getCategoriaGasto().getId());
            gastosDto.add(gastoDTO);
        });
        response.put("ultimosGastos", objectMapper.valueToTree(gastosDto));

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
            Gastos gasto = new Gastos(gastoDTO.getNombre(), gastoDTO.getPrecio(), gastoDTO.getCuotas(), persona.get(), gastoDTO.getFecha(), categoriaGasto.get());
            repository.save(gasto);
            response.put("message", "Gasto guardado");
            response.put("gasto", objectMapper.valueToTree(gastoDTO));
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

    @Override
    public ObjectNode gastosPorCategoria(int idPersona, LocalDate fechaInicio, LocalDate fechaFin) {
        ObjectNode response = objectMapper.createObjectNode();
        List<Gastos> listaTotal = new ArrayList<>();
        List<CategoriaGasto> categorias = categoriaRepository.findAll();
        Optional<Persona> persona = personaRepository.findById(idPersona);
        if(persona.isPresent()){
            Persona person = persona.get();
            listaTotal = repository.findByFechaBetweenAndPersona(fechaInicio, fechaFin, person);
        } else {
            throw new RequestException("Persona no existe", 401, false, HttpStatus.BAD_REQUEST);
        }
        List<CategoriaTotal> totalPerCategory = getTotalPerCategory(listaTotal, categorias);
        response.put("gastosPorCategorias", objectMapper.valueToTree(totalPerCategory));
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

    @Override
    public ObjectNode gastosAhorrosPorMes(int idPersona, int year) {
        ObjectNode response = objectMapper.createObjectNode();
        Optional<Persona> person = personaRepository.findById(idPersona);
        if(!person.isPresent()) {
            throw new RequestException("Persona no existe", 401, false, HttpStatus.BAD_REQUEST);
        }
        List<ObjectNode> listaInfoPorMes = new ArrayList<>();
        for (int i = 1; i <= 12; i++){
            LocalDate fechaInicio = LocalDate.of(year, i, 1);
            LocalDate fechaFin = fechaInicio.withDayOfMonth(fechaInicio.lengthOfMonth());

            List<Gastos> listaTotal = repository.findByFechaBetweenAndPersona(fechaInicio, fechaFin, person.get());
            double sumaTotal = listaTotal.stream().mapToDouble(Gastos::getPrecio).sum();

            List<Ahorros> listaTotalAhorros = ahorroRepository.findByFechaBetweenAndPersona(fechaInicio, fechaFin, person.get());
            double sumaTotalAhorros = listaTotalAhorros.stream().mapToDouble(Ahorros::getMonto).sum();
            ObjectNode infoMes = objectMapper.createObjectNode();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", new Locale("es", "ES"));
            infoMes.put("mes", fechaInicio.format(formatter));
            infoMes.put("gastoTotal", sumaTotal);
            infoMes.put("ahorroTotal", sumaTotalAhorros);
            infoMes.put("year", year);
            listaInfoPorMes.add(infoMes);
        }

        response.put("gastosAhorrosPorMes", objectMapper.valueToTree(listaInfoPorMes));

        return response;
    }
}
