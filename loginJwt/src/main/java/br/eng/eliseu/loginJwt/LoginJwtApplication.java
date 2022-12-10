package br.eng.eliseu.loginJwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @EnableGlobalMethodSecurity, habilita a seguranca do metodo.
 * A propriedade prePostEnabled ativa anotações pré/pós do Spring Security.
 * A propriedade secureEnabled determina se a anotação @Secured deve ser habilitada.
 * A propriedade jsr250Enabled nos permite usar a anotação @RoleAllowed .
 * Ref: https://www.baeldung.com/spring-security-method-security
 */

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class LoginJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginJwtApplication.class, args);
	}

}
