package br.eng.eliseu.loginJwt.contoller;

import br.eng.eliseu.loginJwt.model.Usuario;
import br.eng.eliseu.loginJwt.model.vo.CustomPermission;
import br.eng.eliseu.loginJwt.repository.UsuarioRepository;
import br.eng.eliseu.loginJwt.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;



//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
//@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;



    @GetMapping()
//    @PreAuthorize("hasAuthority('READ')")
//    @PreAuthorize("hasRole('ADMIN')")
//      @PreAuthorize("hasRole('ADMIN') and hasPermission('hasAccess','WRITE')")
//    @PreAuthorize("hasRole(T(com.bs.dmsbox.api.constants.RoleConstants).ROLE_AGENT) " +
//            "|| hasRole(T(com.bs.dmsbox.api.constants.RoleConstants).ROLE_ADMIN)" +
//            "|| (hasRole(T(com.bs.dmsbox.api.constants.RoleConstants).ROLE_CUSTOMER) && #userId == principal.username)")
//    @PreAuthorize("hasPermission(#CustomPermission.build('ADMIN', 'DIRETOR', 'TELA_PRESENCA'), 'READ')");
//    @PreAuthorize("hasPermission(#CustomPermission.build('ADMIN', 'DIRETOR', 'TELA_PRESENCA'), 'READ')")
    public ResponseEntity<Usuario> usuarioByCookie(HttpServletResponse response, HttpServletRequest request){

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

        // nao encontrou
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

    }

    @GetMapping("/listarTodos")
    public ResponseEntity<List<Usuario>> listarUsuarios(){

        return ResponseEntity.ok(usuarioRepository.findAll());

    }

    CustomPermission p = CustomPermission.build("ADMIN", "DIRETOR");

    @GetMapping("/{id}")
//    @PreAuthorize("hasPermission(#id, 'ROLE_ADMIN')")
//    @PreAuthorize("hasPermission(p, 'READ')")
    public ResponseEntity<Usuario> usuarioById(@PathVariable Integer id){

        return ResponseEntity.ok(usuarioRepository.findById(id).get());

    }

}
