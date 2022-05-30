package br.eng.eliseu.loginJwt.model;

import br.eng.eliseu.loginJwt.model.UsuarioModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Detalhes do usuario
 */
public class UsuarioDetalhe implements UserDetails {

    private final Optional<UsuarioModel> usuario;

    public UsuarioDetalhe(Optional<UsuarioModel> usuario) {
        this.usuario = usuario;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Este metodo retorna uma lista de perfil do usuario, e esta classe deve extender GrantedAuthority
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return usuario.orElse(new UsuarioModel()).getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.orElse(new UsuarioModel()).getLogin();
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
