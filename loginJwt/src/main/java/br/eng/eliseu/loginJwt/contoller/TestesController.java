package br.eng.eliseu.loginJwt.contoller;

import br.eng.eliseu.loginJwt.model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/testes")
public class TestesController {

//    @Autowired
//    private UsuarioRepository usuarioRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;


    @GetMapping("/listarTodos")
    public ResponseEntity<List<Usuario>> listarUsuarios(){

//        return ResponseEntity.ok(usuarioRepository.findAll());
        return null;
    }




}
