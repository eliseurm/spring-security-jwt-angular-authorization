package br.eng.eliseu.loginJwt.security;

import br.eng.eliseu.loginJwt.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe responsavel por fazer o build (criar e setar valores) no UserDetails.
 * Note que nao é um servico, entao vamos considera-la apenas como auxiliar. :P
 */

public class UsuarioDetailImpl implements UserDetails {

    private Usuario usuario;

    public UsuarioDetailImpl(Usuario usuario) {
        this.usuario = usuario;
    }

    public static UserDetails build(Usuario usuario) {
        return new UsuarioDetailImpl(usuario);
    }

    private Set<GrantedAuthority> getGrantedAuthorities(Usuario user) {
        Set<GrantedAuthority> autorizacoes = user.getPapeis().stream()
                .map(role -> new SimpleGrantedAuthority(role.getPapel().name()))
                .collect(Collectors.toSet());
        return autorizacoes;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthorities(usuario);
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getNome();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
