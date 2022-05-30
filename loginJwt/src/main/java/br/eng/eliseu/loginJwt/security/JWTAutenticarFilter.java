package br.eng.eliseu.loginJwt.security;

import br.eng.eliseu.loginJwt.model.UsuarioDetalhe;
import br.eng.eliseu.loginJwt.model.UsuarioModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAutenticarFilter extends UsernamePasswordAuthenticationFilter {

    public static final int TOKEN_EXPIRACAO = 600_000;
    public static final String TOKEN_SENHA = "senhaSecretaUnica";

    private final AuthenticationManager authenticationManager;

    public JWTAutenticarFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // --- Pea os dados que vieram na requisicao e convertendo para o tipo UsuarioModel
            UsuarioModel usuario = new ObjectMapper().readValue(request.getInputStream(), UsuarioModel.class);

            UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getPassword(), new ArrayList<>());

            // --- o metodo 'authenticate' é responsavel por verificar se seu usuario e senha sao validos ou nao
            return authenticationManager.authenticate(authenticate);

        } catch (IOException e) {
            throw new RuntimeException("Falha ao autenticar usuario", e);
        }
    }

    /**
     * Se a autenticacao for bem sucedida, neste metodo é gerado um token jwt.
     * Note que a informacao do usuario é colocada no Subject do jwt
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        // --- Note que o parametro 'authResult' é o que o metodo anterior (attemptAuthentication) retornou, e nele nos colocamos as informacoes do usuario
        UsuarioDetalhe usuarioData = (UsuarioDetalhe) authResult.getPrincipal();

        String token = JWT.create()
                .withSubject(usuarioData.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+TOKEN_EXPIRACAO))
                .sign(Algorithm.HMAC512(TOKEN_SENHA));

//        response.setContentType("text/plain;charset=UTF-8");
//        response.getWriter().write(token);

//        response.getWriter().write("{\"Authorization\": \""+token+"\"}");

        response.addHeader("Authorization", "Bearer "+token);

        response.getWriter().flush();

    }
}
