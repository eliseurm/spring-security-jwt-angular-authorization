package br.eng.eliseu.loginJwt.model.vo;

import br.eng.eliseu.loginJwt.model.Usuario;
import br.eng.eliseu.loginJwt.security.UsuarioDetailImpl;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomPermission extends DefaultMethodSecurityExpressionHandler {

    private String papel;

    private String vinculo;

    private String[] telas;

    public CustomPermission(String papel, String vinculo, String[] telas) {
        this.papel = papel;
        this.vinculo = vinculo;
        this.telas = telas;
    }

    public static CustomPermission build(String papel) {
        return new CustomPermission(papel, null, null);
    }

    public static CustomPermission build(String papel, String vinculo) {
        return new CustomPermission(papel, vinculo, null);
    }

    public static CustomPermission build(String papel, String vinculo, String[] telas) {
        return new CustomPermission(papel, vinculo, telas);
    }


    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public String getVinculo() {
        return vinculo;
    }

    public void setVinculo(String vinculo) {
        this.vinculo = vinculo;
    }

    public String[] getTelas() {
        return telas;
    }

    public void setTelas(String[] telas) {
        this.telas = telas;
    }
}
