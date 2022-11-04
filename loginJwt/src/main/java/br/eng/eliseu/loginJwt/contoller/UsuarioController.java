package br.eng.eliseu.loginJwt.contoller;

import br.eng.eliseu.loginJwt.model.Usuario;
import br.eng.eliseu.loginJwt.repository.UsuarioRepository;
import br.eng.eliseu.loginJwt.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

//    @Autowired
//    private PasswordEncoder passwordEncoder;


    @GetMapping()
    public ResponseEntity<Usuario> usuario(@CookieValue(name = "jwtToken") String jwt){
        Optional<Usuario> usuario = Optional.empty();

        // pega o usuario que esta no cookie e retorna os dados dele
        if (jwt != null && jwtUtil.validateJwtToken(jwt)) {
            String username = jwtUtil.getUserNameFromJwtToken(jwt);
            usuario = usuarioRepository.findByNome(username);
        }

        return ResponseEntity.ok(usuario.get());
    }

    @GetMapping("/listarTodos")
    public ResponseEntity<List<Usuario>> listarUsuarios(){

        return ResponseEntity.ok(usuarioRepository.findAll());
//        return null;
    }




}
