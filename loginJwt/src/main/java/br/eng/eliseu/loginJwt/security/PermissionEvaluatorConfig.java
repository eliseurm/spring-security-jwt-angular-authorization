package br.eng.eliseu.loginJwt.security;

import br.eng.eliseu.loginJwt.security.components.PermissionEvaluatorManager;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.authorization.method.PreAuthorizeAuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableMethodSecurity(prePostEnabled = false)
class PermissionEvaluatorConfig {

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE) // define o nivel de relevancia deste Bean (relevancia, APPLICATION(default), SUPPORT, INFRASTRUCTURE respectivamente)
	Advisor preAuthorizeAuthorizationMethodInterceptor(PermissionEvaluatorManager customPermissionEvaluator) {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setPermissionEvaluator(customPermissionEvaluator);

		PreAuthorizeAuthorizationManager authorizationManager = new PreAuthorizeAuthorizationManager();
		authorizationManager.setExpressionHandler(expressionHandler);

		return AuthorizationManagerBeforeMethodInterceptor.preAuthorize(authorizationManager);
	}


/*
	@Bean
	public PermissionEvaluator permissionEvaluator() {
		Map<String, PermissionEvaluator> map = new HashMap<>();

		// Build lookup table of PermissionEvaluator by supported target type
		for (TargetedPermissionEvaluator permissionEvaluator : permissionEvaluators) {
			map.put(permissionEvaluator.getTargetType(), permissionEvaluator);
		}

		return new PermissionEvaluatorManager(map);
	}
*/

}