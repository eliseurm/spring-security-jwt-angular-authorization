package br.eng.eliseu.loginJwt.security;

import br.eng.eliseu.loginJwt.model.Papel;
import br.eng.eliseu.loginJwt.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Detalhes do usuario
 */
public class UsuarioDetalheImpl implements UserDetails {

    private Optional<Usuario> usuario = Optional.empty();


    public UsuarioDetalheImpl(Integer id, String usuario, String email, String senha, Set<String> papeis) {

        this.usuario = Optional.of(new Usuario(id, usuario, email, senha, papeis));
    }


    public static UsuarioDetalheImpl build(Usuario user) {
        Set<String> papeis = user.getPapeis().stream()
                .map(x -> x.getPapel().name())
                .collect(Collectors.toSet());
        return new UsuarioDetalheImpl(user.getId(), user.getNome(), user.getEmail(),user.getSenha(), papeis);

    }

    public static UsuarioDetalheImpl build(Optional<Usuario> optionalUser) {

        return build(optionalUser.get());

    }

    public static List<GrantedAuthority> getGrantedAuthorities(Usuario user) {
        List<GrantedAuthority> autorizacoes = user.getPapeis().stream()
                .map(role -> new SimpleGrantedAuthority(role.getPapel().name()))
                .collect(Collectors.toList());
        return autorizacoes;
    }

    public static Set<Papel> getUsuarioPapel(List<GrantedAuthority> autorizacoes) {
        Set<Papel> papeis = autorizacoes.stream()
                .map(papel -> new Papel(null, papel.getAuthority()))
                .collect(Collectors.toSet());
        return papeis;
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return getGrantedAuthorities(usuario.get());
    }

    @Override
    public String getPassword() {

        return usuario.orElse(new Usuario()).getSenha();
    }

    @Override
    public String getUsername() {

        return usuario.orElse(new Usuario()).getNome();
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





    public Integer getId() {
        return usuario.orElse(new Usuario()).getId();
    }

}
