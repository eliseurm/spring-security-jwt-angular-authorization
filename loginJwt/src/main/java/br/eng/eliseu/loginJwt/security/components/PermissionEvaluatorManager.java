package br.eng.eliseu.loginJwt.security.components;

import br.eng.eliseu.loginJwt.model.vo.CustomPermission;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.DenyAllPermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Esta classe customiza as permissoes sobre os metodos do controller
 *
 * Aqui temos os metodos mais usados do spring
 * hasAnyRole('ADMIN, USER'): Retorna true se o principal atual tiver o papel especificada. Na lista de autoridades do UserDetail, o papel deve sempre comecar com ROLE_.
 * hasAnyAuthority('READ, WRITE'): Retorna true se o principal atual tiver a autoridade especificada. Na lista de autoridades do UserDetail, a autorizacao NAO deve ter ROLE_.
 *
 * E aqui os metodos personalizados
 * hasPermission(Object target, Object permission): Retorna true se o usuário tiver acesso ao destino fornecido para a permissão fornecida.
 * Por exemplo, hasPermission(domainObject, 'read')
 * hasPermission(Object targetId, String targetType, Object permission): Retorna truese o usuário tiver acesso ao destino fornecido para a permissão fornecida.
 * Por exemplo, hasPermission(1, 'com.example.domain.Message', 'read')
 *
 * referencia:
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/html/el-access.html
 */

@Component
public class PermissionEvaluatorManager implements PermissionEvaluator {

//    AQUI: implementar o primeiro metodo hasPermission passando como parametro uma objeto da classe CustomPermission
//    Nesta classe deve ter os seguintes parametro
//    Vou abandonar estas permisoes, por enquanto porque nao consegui pensar em algo que me service.
//    fica aqui um link bem interecante que vale a pena ser estudade com mais afinco
//    https://www.baeldung.com/role-and-privilege-for-spring-security-registration


    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        List<String> papeis = authentication.getAuthorities().stream()
                .map(x -> (String)x.getAuthority())
                .collect(Collectors.toList());

        boolean ok = false;
        if (targetDomainObject instanceof CustomPermission) {
            CustomPermission custom = (CustomPermission)targetDomainObject;

            // aqui precisa defender os null
//            if(papeis.stream().anyMatch(ga -> ga.getAuthority().equals(custom.getPapel()))){
//                ok = true;
//            }
//            if(papeis.stream().anyMatch(ga -> comparaVinculo(ga.getAuthority(), custom.getVinculo()))){
//                ok = ok && true;
//            }

            // aqui implementa custom.getTelas()
        }

        return ok;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }

    private Boolean comparaVinculo( String a, String b){
        // aqui seria implementado a logica segundo os valores de cada vinculo

        return a.compareTo(b)<=0;
    }



/*

    // Referencia: https://github.com/timtebeek/spring-security-samples/tree/f3877110c0c58efe5da9a16b1305ec32a3b4e14b/permission-evaluator

    private final SpreadsheetPermissionStore store;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        User principal = (User) authentication.getPrincipal();

        if (targetDomainObject instanceof Spreadsheet) {
            return hasSpreadsheetPermission(principal, (Spreadsheet) targetDomainObject, permission);
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        User principal = (User) authentication.getPrincipal();

        if (targetType.equals(Spreadsheet.class.getName())) {
            return hasSpreadsheetPermission(principal, targetId, permission);
        }

        return false;
    }

    private boolean hasSpreadsheetPermission(User principal, Spreadsheet spreadsheet, Object permission) {
        boolean hasPermission = store.getPermissions().stream().anyMatch(p -> p.getUser().equals(principal)
                && p.getSpreadsheet().equals(spreadsheet)
                && p.getLevel().equals(permission));
        if (!hasPermission) {
            log.warn("Denying {} {} access to {}", principal, permission, spreadsheet);
        }
        return hasPermission;
    }

    private boolean hasSpreadsheetPermission(User principal, Serializable targetId, Object permission) {
        boolean hasPermission = store.getPermissions().stream().anyMatch(p -> p.getUser().equals(principal)
                && p.getSpreadsheet().getId().equals(targetId)
                && p.getLevel().equals(permission));
        if (!hasPermission) {
            log.warn("Denying {} {} access to Spreadsheet with id {}", principal, permission, targetId);
        }
        return hasPermission;
    }
*/



/*

    // referencia: https://insource.io/blog/articles/custom-authorization-with-spring-boot.html

    private static final PermissionEvaluator denyAll = new DenyAllPermissionEvaluator();

    private final Map<String, PermissionEvaluator> permissionEvaluators;

    public PermissionEvaluatorManager(Map<String, PermissionEvaluator> permissionEvaluators) {
        // esta injecao vem do PermissionEvaluatorConfig
        this.permissionEvaluators = permissionEvaluators;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        PermissionEvaluator permissionEvaluator = permissionEvaluators.get(targetDomainObject.getClass().getSimpleName());
        if (permissionEvaluator == null) {
            permissionEvaluator = denyAll;
        }

        return permissionEvaluator.hasPermission(authentication, targetDomainObject, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        PermissionEvaluator permissionEvaluator = permissionEvaluators.get(targetType);
        if (permissionEvaluator == null) {
            permissionEvaluator = denyAll;
        }

        return permissionEvaluator.hasPermission(authentication, targetId, targetType, permission);
    }

    private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
        for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
            if (grantedAuth.getAuthority().startsWith(targetType) &&
                    grantedAuth.getAuthority().contains(permission)) {
                return true;
            }
        }
        return false;
    }
*/
}