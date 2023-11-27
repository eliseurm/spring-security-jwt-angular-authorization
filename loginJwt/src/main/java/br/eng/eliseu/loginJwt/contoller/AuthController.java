package br.eng.eliseu.loginJwt.contoller;

import br.eng.eliseu.loginJwt.LoginJwtApplication;
import br.eng.eliseu.loginJwt.model.Papel;
import br.eng.eliseu.loginJwt.model.PapelEnum;
import br.eng.eliseu.loginJwt.model.Usuario;
import br.eng.eliseu.loginJwt.model.vo.JwtResponse;
import br.eng.eliseu.loginJwt.model.vo.LoginRequestVO;
import br.eng.eliseu.loginJwt.model.vo.MessageResponse;
import br.eng.eliseu.loginJwt.model.vo.SaveRequestVO;
import br.eng.eliseu.loginJwt.repository.PapelRepository;
import br.eng.eliseu.loginJwt.repository.UsuarioRepository;
import br.eng.eliseu.loginJwt.security.UsuarioDetailImpl;
import br.eng.eliseu.loginJwt.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(LoginJwtApplication.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PapelRepository papelRepository;


    /**
     * Autentica um usuario
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> autoriza(@RequestBody LoginRequestVO loginRequestVO, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestVO.getNome(), loginRequestVO.getSenha()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UsuarioDetailImpl userDetails = (UsuarioDetailImpl) authentication.getPrincipal();

        // Gera token jwt a partir das informacoes do usuario autorizado
        // comentei porque no metodo que gera o cookie tbm gera o token com este mesmo metodo
//        String jwt = jwtUtil.generateJwtToken(userDetails);

        // Monto uma lista com as autorizacoes do usuario autenticado
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        logger.info("Usuario: {}, papel: {}", userDetails.getUsername(), roles);

        // Monto o cookie que vai ser passado para o navegador
//        ResponseCookie jwtCookie = jwtUtil.generateJwtResponseCookie(userDetails);

        Cookie cookie = jwtUtil.generateJwtCookie(userDetails);

        response.addCookie(cookie);

        // Carrego a classe que vai ser usada para retornar as informacoes do login
        JwtResponse res = new JwtResponse();
//        res.setToken(jwtCookie.getValue());
//        res.setId(userDetails.getId());
        res.setUsuario(userDetails.getUsername());
        res.setPapeis(roles);

        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(res)
                ;
//        return ResponseEntity.ok(res);
//        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtil.getCleanJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("Você esta fora!"));
    }

    @PostMapping("/save")
    @Transactional
    public ResponseEntity<String> salva(@RequestBody SaveRequestVO saveRequestVO) {

        if (usuarioRepository.existsByNome(saveRequestVO.getNome())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este usuario ja esta cadastrado");
        }
        if (usuarioRepository.existsByEmail(saveRequestVO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ja existe um usuario com este email");
        }

        String hashedPassword = passwordEncoder.encode(saveRequestVO.getSenha());
        Set<Papel> roles = new HashSet<>();
        List<Papel> userRole = papelRepository.findAll();
        if (userRole.isEmpty()) {
            roles.add(new Papel(null, PapelEnum.ROLE_ADMIN.name()));
            roles.add(new Papel(null, PapelEnum.ROLE_USER.name()));
            roles.add(new Papel(null, PapelEnum.ROLE_MODERATOR.name()));
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("role not found");
        }
        roles.addAll(userRole);

        Usuario user = new Usuario();
        user.setNome(saveRequestVO.getNome());
        user.setEmail(saveRequestVO.getEmail());
        user.setSenha(hashedPassword);
        user.setPapeis(roles);

        usuarioRepository.save(user);

        return ResponseEntity.ok("Usuário registrado com sucesso");
    }

    @GetMapping("/validaSenha")
    public ResponseEntity<Boolean> validaSenha(@RequestBody LoginRequestVO loginRequestVO){
        Optional<Usuario> optUsuario = usuarioRepository.findByNome(loginRequestVO.getNome());
//        Optional<Usuario> optUsuario = Optional.of(new Usuario(null, "admin", "@email", "$2a$12$Yofga1.Zqg36R2oYjIWPOuIDY0r6xaL./2N2eAqv1iY1lR.tOC2La", new HashSet<String>(Arrays.asList("ROLE_ADMIN"))));

        if(optUsuario.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        Usuario usuario = optUsuario.get();
        boolean valid = passwordEncoder.matches(usuario.getNome(), usuario.getSenha());

        System.out.println("armazenado: " + usuario.getNome() + ":" + usuario.getSenha());
        System.out.println("nova senha: 123456:" + passwordEncoder.encode("123456"));


        HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(valid);

//        return null;
    }


}
