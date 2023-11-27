package br.eng.eliseu.loginJwt.security.components;

import br.eng.eliseu.loginJwt.model.Usuario;
import br.eng.eliseu.loginJwt.repository.UsuarioRepository;
import br.eng.eliseu.loginJwt.security.UsuarioDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servico responsavel por carregar o usuario do banco
 */

@Service
public class UsuarioDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String nomeUsuario) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByNome(nomeUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado. "+nomeUsuario));

        return UsuarioDetailImpl.build(usuario);
//        return usuario;
    }
}
