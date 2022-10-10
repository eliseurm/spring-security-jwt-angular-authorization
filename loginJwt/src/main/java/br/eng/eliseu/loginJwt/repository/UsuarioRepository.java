package br.eng.eliseu.loginJwt.repository;

import br.eng.eliseu.loginJwt.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByNome(String nome);
    Boolean existsByNome(String nome);
    Boolean existsByEmail(String email);
}
