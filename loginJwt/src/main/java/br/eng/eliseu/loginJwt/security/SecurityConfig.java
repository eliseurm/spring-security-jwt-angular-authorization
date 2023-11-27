package br.eng.eliseu.loginJwt.security;


import br.eng.eliseu.loginJwt.security.components.AuthenticationEntryManager;
import br.eng.eliseu.loginJwt.security.components.UsuarioDetailServiceImpl;
import br.eng.eliseu.loginJwt.security.components.CookieTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
//@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // habilita o controle de restricoes diretamente nos controllers
public class SecurityConfig {

    @Autowired
    private UsuarioDetailServiceImpl usuarioDetalheService;

    @Autowired
    private AuthenticationEntryManager authenticationEntryManager;

    @Autowired
//    private HeaderTokenFilter authTokenFilter;
    private CookieTokenFilter authTokenFilter;

    @Autowired
    JwtUtil jwtUtil;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//        return new MyPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // AuthenticationManagerBuilder permite criar autenticação de memória e em JDBC e adicionar UserDetailsService e AuthenticationProvider's.
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    @Primary
    public AuthenticationManagerBuilder configureAuthenticationManagerBuilder(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(usuarioDetalheService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors()
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryManager) // trata as exceptions, chama AuthEntryPointImpl para personalizar o erro de resposta
                .and()
//                .httpBasic()// quando utilizado usa o usuario e senha do formulario basico
//                .and()
//                .addFilterBefore(authTokenFilter, BasicAuthenticationFilter.class) // filtro para validar o token que esta no cookie
//                .addFilterBefore(new UsernamePasswordAuthenticationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// faz o spring checar (UsuarioDetailServiceImpl) o usuario em todas as requisicoes
                .and()
                .authorizeRequests()
                // aqui faz as restricoes diretamente nos filters, elas tbm podem ser feitas diretamente no controles, as definidas no controler teem precedencia a estas aqui
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/usuario/**").permitAll()
//                .antMatchers("/testes/**").permitAll()
//                .antMatchers(HttpMethod.GET).hasAnyAuthority("READ", "WRITE")
//                .antMatchers(HttpMethod.GET).hasAnyRole("ADMIN", "USER")
//                .antMatchers(HttpMethod.POST).hasRole("ADMIN") // novo
//                .antMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("ADMIN", "USER") // altera
//                .antMatchers(HttpMethod.DELETE).hasRole("ADMIN") // o ** é porque eu preciso pasar o id no delete
                .anyRequest().authenticated()
        ;

        http.logout().permitAll().deleteCookies(jwtUtil.getJwtCookieName());

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
//        configuration.addAllowedOriginPattern("*");
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


/**
 * No fluxo de filters do spring, por padrao, os filtros sao chamados duas vezes, uma antes do controller e outra depois
 * filter1
 *   filter2
 *     filter3
 *       Controller
 *     filter3
 *   filter2
 * filter1
 * O OncePerRequestFilter, indica que este filtro vai ser chamado apenas uma vez por requisicao, e esta vez é antes do controller
 */




}
