package es.pcomida.proyectocomida.auth.services.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    // Hemos quitado la inyección de @Value para el diagnóstico.
    // La clave y la expiración ahora están escritas directamente en el código.

    private final int jwtExpiration = 86400000; // 1 día en milisegundos

    @Override
    public String extractUserName(String token) {
        return getClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        // SOLUCIÓN DE DIAGNÓSTICO:
        // Escribimos la clave secreta directamente aquí para garantizar que es
        // absolutamente la misma para firmar y para verificar.
        String jwtSecret = "MeGustanLosTacosDeSuaperroPeroNoMeGustaLaSalsaVerdePorqueMePicaMuchoYMeHaceLlorar";
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
