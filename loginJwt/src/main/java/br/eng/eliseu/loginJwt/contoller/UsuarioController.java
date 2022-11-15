package br.eng.eliseu.loginJwt.contoller;

import br.eng.eliseu.loginJwt.model.Usuario;
import br.eng.eliseu.loginJwt.repository.UsuarioRepository;
import br.eng.eliseu.loginJwt.security.jwt.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;



//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;



    @GetMapping()
    public ResponseEntity<Usuario> usuario(HttpServletResponse response, HttpServletRequest request){

        Optional<Usuario> usuario = Optional.empty();

        // pega o usuario que esta no cookie e retorna os dados dele
        String jwt = jwtUtil.getJwtFromCookies(request);
        try {
            if (jwt != null && jwtUtil.validateJwtToken(jwt)) {
                String username = jwtUtil.getUserNameFromJwtToken(jwt);
                usuario = usuarioRepository.findByNome(username);
                return ResponseEntity.ok(usuario.get());
            }
        } catch (Exception e) {
            logger.error("Não conseguiu encontrar informações sobre o usuário. {}", e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

    }

    @GetMapping("/listarTodos")
    public ResponseEntity<List<Usuario>> listarUsuarios(){

        return ResponseEntity.ok(usuarioRepository.findAll());

    }

}
