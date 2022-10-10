package br.eng.eliseu.loginJwt.utils;

import br.eng.eliseu.loginJwt.security.UsuarioDetalheImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${fullstackbook.app.jwtSecret}")
    private String jwtSecret;

    @Value("${fullstackbook.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UsuarioDetalheImpl userPrincipal = (UsuarioDetalheImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

    }

    public String getUserNameFromJwtToken(String token) {

        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();

    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            System.out.print(e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.print(e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.print(e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.print(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.print(e.getMessage());
        }
        return false;
    }
}
