package br.eng.eliseu.loginJwt.repository;

import br.eng.eliseu.loginJwt.model.Papel;
import br.eng.eliseu.loginJwt.model.PapelEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PapelRepository extends JpaRepository<Papel, Integer> {

    public Optional<Papel> findByPapel(PapelEnum papel);

}
