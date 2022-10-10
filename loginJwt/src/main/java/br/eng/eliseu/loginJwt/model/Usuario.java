package br.eng.eliseu.loginJwt.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String nome;

    @Column
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // impede que a senha seja levada junto com o Json
    private String senha;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "usuarios_to_papeis",
            joinColumns = @JoinColumn(name="usuario_id"),
            inverseJoinColumns = @JoinColumn(name="papel_id"))
    private Set<Papel> papeis = new HashSet<>();

    public Usuario() {
    }

    public Usuario(Integer id, String nome, String email, String senha, Set<String> papeis) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.papeis = papeis.stream().map(x -> new Papel(null, x)).collect(Collectors.toSet());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String usuario) {
        this.nome = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Set<Papel> getPapeis() {
        return papeis;
    }

    public void setPapeis(Set<Papel> papeis) {
        this.papeis = papeis;
    }
}
