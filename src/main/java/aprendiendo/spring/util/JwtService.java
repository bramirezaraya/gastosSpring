package aprendiendo.spring.util;
import aprendiendo.spring.Models.TokenValidationResult;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 36000000)) // 10 horas
                .signWith(key)
                .compact();
    }

    public String validateTokenAndGetSubject(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            // Token expirado
            throw new RuntimeException("Token expirado", e);
        } catch (JwtException e) {
            // Token inválido (firma incorrecta, manipulado, etc.)
            throw new RuntimeException("Token inválido", e);
        }
    }

    public TokenValidationResult ValidateToken(String token) {
        try {
            String subject = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return new TokenValidationResult(true, "Token válido", subject);
        } catch (ExpiredJwtException e) {
            return new TokenValidationResult(false, "Token expirado", null);
        } catch (JwtException e) {
            return new TokenValidationResult(false, "Token inválido", null);
        }
    }
}
