package br.eng.eliseu.loginJwt.contoller;

import br.eng.eliseu.loginJwt.model.UsuarioModel;
import br.eng.eliseu.loginJwt.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

//@RestController
//@RequestMapping("/auth")
public class AuthController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity salvar(@Validated @RequestBody UsuarioModel usuario){
        //            String username = data.getEmail();
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
//            String token = jwtTokenProvider.createToken(username, this.users.findByEmail(username).getRoles());
        Map<Object, Object> model = new HashMap<>();
//            model.put("username", username);
//            model.put("token", token);
        return ResponseEntity.ok(model);
    }

}
