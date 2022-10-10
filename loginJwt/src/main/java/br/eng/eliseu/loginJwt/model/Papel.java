package br.eng.eliseu.loginJwt.model;

import javax.persistence.*;

@Entity
@Table(name = "papeis")
public class Papel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @Enumerated(EnumType.STRING)
    private PapelEnum papel;


    public Papel() {
    }

    public Papel(Integer id, String papel) {
        this.id = id;
        this.papel = PapelEnum.valueOf(papel);
    }





    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PapelEnum getPapel() {
        return papel;
    }

    public void setPapel(PapelEnum papel) {
        this.papel = papel;
    }
}
