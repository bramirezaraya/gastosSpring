package aprendiendo.spring.util;
import aprendiendo.spring.Exception.RequestException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // ✅ Permitir siempre CORS para todas las respuestas
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        try {
            String header = request.getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {
                throw new RequestException("Token no informado o mal formado", 401, false, HttpStatus.UNAUTHORIZED);
            }

            String token = header.substring(7);
            try {
                String email = jwtService.validateTokenAndGetSubject(token);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JwtException e) {
                throw new RequestException("Token inválido", 401, false, HttpStatus.UNAUTHORIZED);
            }

            filterChain.doFilter(request, response);
        }  catch (RequestException e) {
            // Escribimos manualmente la respuesta para que llegue el JSON a Postman
            response.setStatus(e.getHttpStatus().value());
            response.setContentType("application/json");
            String json = String.format("""
            {
              "message": "%s",
              "status": %s,
              "code": %d
            }
            """, e.getMessage(), e.isStatus(), e.getCode());

            response.getWriter().write(json);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        //permite login y register sin token.
        String path = request.getRequestURI();
        return request.getMethod().equalsIgnoreCase("OPTIONS") ||
                path.equals("/auth/login") || path.equals("/auth/register")
                || path.equals("/auth/verifyToken");
    }
}
