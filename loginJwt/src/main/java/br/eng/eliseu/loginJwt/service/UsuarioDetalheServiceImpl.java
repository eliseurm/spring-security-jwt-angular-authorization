package br.eng.eliseu.loginJwt.service;

import br.eng.eliseu.loginJwt.model.UsuarioDetalhe;
import br.eng.eliseu.loginJwt.model.UsuarioModel;
import br.eng.eliseu.loginJwt.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Servico de consulta de usuario do spring
 */
@Component
public class UsuarioDetalheServiceImpl implements UserDetailsService {

    @Autowired private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UsuarioModel> usuario = repository.findByLogin(username);
        if(usuario.isEmpty()){
            throw new UsernameNotFoundException("Usuario ["+username+"] nao encontrado!!");
        }

        return new UsuarioDetalhe(usuario);
    }

}
