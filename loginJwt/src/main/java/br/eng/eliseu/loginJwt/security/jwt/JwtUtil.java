package br.eng.eliseu.loginJwt.security.jwt;

import br.eng.eliseu.loginJwt.security.impl.UsuarioDetalheImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${fullstackbook.app.jwtSecret}")
    private String jwtSecret;

    @Value("${fullstackbook.app.jwtExpirationMs}")
    private int jwtExpirationms;

    @Value("${fullstackbook.app.jwtCookieName}")
    private String jwtCookieName;

    public String generateJwtToken(UsuarioDetalheImpl userPrincipal) {

        String authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationms))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .claim("authorities", authorities)
                .compact();

    }

    public ResponseCookie generateJwtCookie(UsuarioDetalheImpl userPrincipal) {
        String jwt = generateJwtToken(userPrincipal);
        ResponseCookie cookie = ResponseCookie
                .from(jwtCookieName, jwt)
                .path("/api")
                .maxAge(3600) // 1h
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
            logger.error("Assinatura JWT inválida: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("O token JWT expirou: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("O token JWT não é compatível: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("A string de declarações (claims) JWT está vazia: {}", e.getMessage());
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
