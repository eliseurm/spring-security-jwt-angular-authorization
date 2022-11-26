import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {AuthService} from "./auth.service";

@Injectable()
export class AuthGuardService implements CanActivate {

  constructor(private router: Router,
              private authService: AuthService) {
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    //--> https://github.com/robisim74/AngularSPAWebAPI/blob/master/ClientApp/src/app/services/auth.guard.ts
    const isLoggedIn = this.authService.loggedIn;
    const isAuthForm = [
      'login-form',
      'reset-password',
      'create-account',
      'change-password/:recoveryCode'
    ].includes(next.routeConfig?.path || this.authService.defaultPath);
    const isRoleOk = this.checkRole(next);

    if (isLoggedIn && isAuthForm && isRoleOk) {
      this.authService.lastAuthenticatedPath = this.authService.defaultPath;
      this.router.navigate([this.authService.defaultPath]);
      return false;
    }

    if (!isLoggedIn && !isAuthForm) {
      this.router.navigate(['/login-form']);
    }

    if (isLoggedIn) {
      this.authService.lastAuthenticatedPath = next.routeConfig?.path || this.authService.defaultPath;
    }

    return isLoggedIn || isAuthForm;
  }

  checkRole(route: ActivatedRouteSnapshot): boolean {
    // Quando o menu nao tiver role definido a rota fica livre
    if(!route.data){
      return true;
    }

    // comparo a rota do menu com as rotas do usuario
    let role = route.data?.['role'];
    let userRoles = this.authService.user?.roles;
    if (role && userRoles && userRoles.indexOf(role) === -1) {
      this.router.navigate([this.authService.defaultPath]);
      return false;
    }
    return true;
  }
}

