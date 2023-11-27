package br.eng.eliseu.loginJwt.security;

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

    public String generateJwtToken(UsuarioDetailImpl userPrincipal) {

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

    /**
     * A classe ResponseCookie so spring é mais segura porque ja tem implementado recurso para 'SameSite Property'
     * A SameSite sao propriedades de um cookie. É usada para restringir cookies de terceiros e, assim, reduzir os riscos de segurança.
     * Opcoes do same: Strict, Lax, None
     * Strict (restrito, nao permite ver o cookie), Lax(relachado, consegue ver algumas coisas do copkie), None(nenhum, ve todo o cookie)
     */
    public ResponseCookie generateJwtResponseCookie(UsuarioDetailImpl userPrincipal) {
        String jwt = generateJwtToken(userPrincipal);
        ResponseCookie cookie = ResponseCookie
                .from(jwtCookieName, jwt)
                .path("/")
                .maxAge(3600) // 1h
                .secure(true)
                .httpOnly(true)
                .sameSite("Lax")
                .build();
        return cookie;
    }


    public Cookie generateJwtCookie(UsuarioDetailImpl userPrincipal) {
        String jwt = generateJwtToken(userPrincipal);
        Cookie cookie = new Cookie(jwtCookieName, jwt);
//        cookie.setDomain("mensageiros.udi.br");
        cookie.setPath("/");
        cookie.setMaxAge(3600); // 1h
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        return cookie;
    }

    // o cokie de limpeza deve ter os mesmos parametros do cookie que sera apagado.
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie
                .from(jwtCookieName, null)
                .path("/")
                .maxAge(0)
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

    public String getJwtCookieName() {
        return jwtCookieName;
    }
}
