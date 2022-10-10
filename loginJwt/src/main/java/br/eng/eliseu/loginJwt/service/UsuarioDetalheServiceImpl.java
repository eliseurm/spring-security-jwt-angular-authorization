package br.eng.eliseu.loginJwt.service;

import br.eng.eliseu.loginJwt.security.UsuarioDetalheImpl;
import br.eng.eliseu.loginJwt.model.Usuario;
import br.eng.eliseu.loginJwt.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

/**
 * Servico de consulta de usuario do spring
 */
@Service
public class UsuarioDetalheServiceImpl implements UserDetailsService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioDetalheServiceImpl() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> usuario = Optional.ofNullable(usuarioRepository.findByNome(username).orElseThrow(() -> new UsernameNotFoundException("Erro ao buscar pelo usuario")));
//        Optional<Usuario> usuario = Optional.of(new Usuario(null, "admin", "@email", "$2a$12$Yofga1.Zqg36R2oYjIWPOuIDY0r6xaL./2N2eAqv1iY1lR.tOC2La", new HashSet<String>(Arrays.asList("ROLE_ADMIN"))));
        if (usuario.isEmpty()) {
            throw new UsernameNotFoundException("Usuario [" + username + "] nao encontrado!!");
        }

        return UsuarioDetalheImpl.build(usuario);
    }

}
