package br.eng.eliseu.loginJwt.security.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationEntryPoint = Ponto de entrada so filtro de seguranca spring
 * Aqui tbm é possivel personalizar o ponto de entrada e respostas
 * Por padrao o spring emite uma pagina html com um 404, mas pode ser personalizado
 * quando for usar o rest full.
 * Esta é a pagina que responde caso a autenticacao nao de certo
 */

@Component
public class AuthenticationEntryManager implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEntryManager.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.print(authException.getMessage());
        logger.info("Tentativa de autenticação não foi bem sucedida: {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }

}

