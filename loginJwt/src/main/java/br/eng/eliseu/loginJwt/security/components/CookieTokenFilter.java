package br.eng.eliseu.loginJwt.security.components;

import br.eng.eliseu.loginJwt.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Busca o token jwt do cookie
 *
 * No fluxo de filters do spring, por padrao, os filtros sao chamados duas vezes, uma antes do controller e outra depois
 * filter1
 *   filter2
 *     filter3
 *       Controller
 *     filter3
 *   filter2
 * filter1
 * O OncePerRequestFilter, indica que este filtro vai ser chamado apenas uma vez por requisicao, e esta vez é antes do controller
 *
 * Ele vai passar aqui sempre que tiver uma requisicao, e vai passar antes de chgar no controller
 * entao, como o /api/auth/login tbm é um dos meus controlers e esta liberado, ele tbm vai passar aqui antes de chegar no loginController
 * tipo, varifica o token atual, e logo em seguida gera um novo token.
 * Bom, pode ser que tenha uma maneira de ter um controllerLogin que chegue direto sem passar por este filtro, enquanto
 * nao descubro, vai ficando assim mesmo.. :)
 *
 **/

@Component
public class CookieTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioDetailServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(CookieTokenFilter.class);
    private AuthenticationFailureHandler failureHandler = new AuthenticationEntryPointFailureHandler(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // voce pode usar um if assim caso nao queir que carregar o cookie quando a requisicao por para o login :P
//        if ("/api/auth/login".equals(request.getServletPath()) && HttpMethod.POST.matches(request.getMethod())) {
//        }

        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtil.validateJwtToken(jwt)) {
                String username = jwtUtil.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // parte do codigo responsavel por fazer a autenticacao do usuario
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (AuthenticationException e) {
            logger.error("Não é possível definir a autenticação do usuário: {}", e);
            this.failureHandler.onAuthenticationFailure(request, response, e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtil.getJwtFromCookies(request);
        return jwt;
    }
}
