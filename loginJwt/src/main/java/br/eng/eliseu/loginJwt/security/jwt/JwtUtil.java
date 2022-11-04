package br.eng.eliseu.loginJwt.security.jwt;

import br.eng.eliseu.loginJwt.security.impl.UsuarioDetalheImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${fullstackbook.app.jwtSecret}")
    private String jwtSecret;

    @Value("${fullstackbook.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("jwtToken")
    private String jwtCookieName;

    public String generateJwtToken(UsuarioDetalheImpl userPrincipal) {

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

    }

    public ResponseCookie generateJwtCookie(UsuarioDetalheImpl userPrincipal) {
        String jwt = generateJwtToken(userPrincipal);
        ResponseCookie cookie = ResponseCookie
                .from(jwtCookieName, jwt)
                .path("/api")
                .maxAge(3600)
                .secure(true)
                .httpOnly(true)
                .build();
        return cookie;
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


    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie
                .from(jwtCookieName, null)
                .path("/api")
                .build();
        return cookie;
    }

    public String getJwtCookieName() {
        return jwtCookieName;
    }
}
