package aprendiendo.spring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ClientInterceptorMethod implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ClientInterceptorMethod.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution clientHttp) throws IOException {
        ClientHttpResponse response = clientHttp.execute(request, body);
        String responseBody = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
        if(response.getStatusCode().value() != 200 || responseBody.contains("BTErrorNegocio")){
            LogsHeaders(request);
            if (body != null && body.length > 0) {
                String requestBody = new String(body, StandardCharsets.UTF_8);
                log.error("[REQUEST BODY] Cuerpo de la solicitud: {}", requestBody);
            }
            log.error("[RESPONSE] Respuesta de la API externa ({}): {} " , response.getStatusCode() , responseBody);
            log.error("---------------------FIN DE ERROR SERVICIO----------------------");
        }
        return response;
    }

    private static void LogsHeaders(HttpRequest request) {
        log.error("Interceptando solicitud de entrada en el [provider]");
        log.error("---------------------INICIO DE ERROR SERVICIO------------------------");
        log.error("Header SERVIDOR ERROR: " + request.getHeaders());
        log.error("Método HTTP: " + request.getMethod());
        log.error("URL SERVIDOR ERROR: " + request.getURI());
    }
}


//QUARKUS
//@Provider
//public class ClientInterceptor implements ClientResponseFilter {
//
//    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(Interceptor.class);
//    @Override
//    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
//
//        if(responseContext.getStatus() != 200){
//            LogsHeaders(requestContext, responseContext);
//            if (responseContext.hasEntity()) {
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                responseContext.getEntityStream().transferTo(baos);
//                String body = baos.toString(StandardCharsets.UTF_8);
//                log.error("Respuesta del servicio: " + body);
//                responseContext.setEntityStream(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)));
//                log.error("---------------------FIN DE ERROR SERVICIO----------------------");
//            }
//        }
//    }
//
//    private static void LogsHeaders(ClientRequestContext requestContext, ClientResponseContext responseContext) {
//        log.error("---------------------INICIO DE ERROR SERVICIO----------------------");
//        log.error("Interceptando solicitud en el provider [ClientInterceptor]");
//        requestContext.getHeaders().forEach((key, value) ->
//                log.error("Header: " + key + " = " + value)
//        );
//        log.error("URL: " + requestContext.getUri());
//        log.error("Método HTTP: " + requestContext.getMethod());
//        log.error("Status: " + responseContext.getStatus());
//    }
//}
