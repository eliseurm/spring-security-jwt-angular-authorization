package br.eng.eliseu.loginJwt.contoller;

import br.eng.eliseu.loginJwt.model.Papel;
import br.eng.eliseu.loginJwt.model.PapelEnum;
import br.eng.eliseu.loginJwt.model.Usuario;
import br.eng.eliseu.loginJwt.repository.PapelRepository;
import br.eng.eliseu.loginJwt.repository.UsuarioRepository;
import br.eng.eliseu.loginJwt.security.UsuarioDetalheImpl;
import br.eng.eliseu.loginJwt.model.vo.JwtResponse;
import br.eng.eliseu.loginJwt.model.vo.LoginRequestVO;
import br.eng.eliseu.loginJwt.model.vo.SaveRequestVO;
import br.eng.eliseu.loginJwt.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

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
    public ResponseEntity<?> autoriza(@RequestBody LoginRequestVO loginRequestVO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestVO.getNome(), loginRequestVO.getSenha()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);

        UsuarioDetalheImpl userDetails = (UsuarioDetalheImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        JwtResponse res = new JwtResponse();
        res.setToken(jwt);
        res.setId(userDetails.getId());
        res.setUsuario(userDetails.getUsername());
        res.setPapeis(roles);
        return ResponseEntity.ok(res);
//        return ResponseEntity.ok("Ok");
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
    public ResponseEntity<Boolean> validaSenha(@RequestParam String nome, @RequestParam String senha){
//        Optional<Usuario> optUsuario = usuarioRepository.findByNome(nome);
        Optional<Usuario> optUsuario = Optional.of(new Usuario(null, "admin", "@email", "$2a$12$Yofga1.Zqg36R2oYjIWPOuIDY0r6xaL./2N2eAqv1iY1lR.tOC2La", new HashSet<String>(Arrays.asList("ROLE_ADMIN"))));

        if(optUsuario.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        Usuario usuario = optUsuario.get();
        boolean valid = passwordEncoder.matches(senha, usuario.getSenha());

        HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(valid);

//        return null;
    }


}
