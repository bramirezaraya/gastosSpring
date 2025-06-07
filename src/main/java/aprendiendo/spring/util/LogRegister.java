package aprendiendo.spring.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogRegister {

    private static final Logger LOG = LoggerFactory.getLogger(LogRegister.class);

    public void entradaController(ObjectNode entrada, String endPoint) {
        LOG.info("-------------------INICIO CONTROLLER-------------------");
        LOG.info("END POINT : " + endPoint);
        LOG.info("Entrada al controlador: {}", entrada);
        LOG.info("-------------------FIN CONTROLLER-------------------");
    }
}
