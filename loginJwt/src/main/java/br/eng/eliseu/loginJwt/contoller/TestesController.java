package br.eng.eliseu.loginJwt.contoller;

import br.eng.eliseu.loginJwt.model.Usuario;
import br.eng.eliseu.loginJwt.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/testes")
public class TestesController {

    @Autowired
    private UsuarioRepository usuarioRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;


    @GetMapping("/listarTodos")
    public ResponseEntity<List<Usuario>> listarUsuarios(){

        return ResponseEntity.ok(usuarioRepository.findAll());
    }


    /**
     * Teste de seguranco no metodo
     * @Secured({ "ROLE_ADMIN", "ROLE_USER" }) e @RolesAllowed({ "ROLE_VIEWER", "ROLE_EDITOR" }) sao equivalentes
     * ainda temos @PreAuthorize("hasAuthority('ROLE_ADMIN')") e @PosAuthorize("hasAuthority('ROLE_ADMIN')")
     * que sao chamadas antes e depois de entrar no metodos, respectivamente.
     * A anotação @Secured não suporta Spring Expression Language (SpEL).
     */
    @Secured({ "ROLE_ADMIN", "ROLE_USER" })
    @GetMapping("/listarTodos2")
    public ResponseEntity<List<Usuario>> listarUsuarios2(){
        return ResponseEntity.ok(usuarioRepository.findAll());
    }




}
