import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router} from '@angular/router';
import {IUser} from "../data/IUser";
import {HttpClient} from "@angular/common/http";
import {firstValueFrom} from "rxjs";
import {IJwtResponse} from "../data/IJwtResponse";

const defaultPath = '/';
const defaultUser = {
  token: '',
  email: 'admin',
  avatarUrl: 'https://js.devexpress.com/Demos/WidgetsGallery/JSDemos/images/employees/06.png'
};

@Injectable()
export class AuthService {

  private _user: IUser | null = null;
  private url = "http://localhost:8080";

  get loggedIn(): boolean {
    return !!this._user;
  }

  private _lastAuthenticatedPath: string = defaultPath;

  set lastAuthenticatedPath(value: string) {
    this._lastAuthenticatedPath = value;
  }

  constructor(private router: Router,
              private http: HttpClient) {

  }

  async logIn(email: string, password: string) {
    // let headers = new HttpHeaders().set('Content-Type', 'application/json');
    let data = {nome: email, senha: password};

    try {
      // Send request
      let data$ = this.http.post<IJwtResponse>(this.url+'/api/auth/login', data);
      let value = await firstValueFrom(data$);

      this._user = new class implements IUser {
        avatarUrl: string = value.token;
        email: string = value.usuario;
        token: string = "https://js.devexpress.com/Demos/WidgetsGallery/JSDemos/images/employees/06.png";
      }

      this.router.navigate([this._lastAuthenticatedPath]);

      return {
        isOk: true,
        data: this._user
      };
    } catch {
      return {
        isOk: false,
        message: "Authentication failed"
      };
    }
  }

  async getUser() {

    try {
      // Send request
      // vai buscar o usuario que esta no cookie
      // AQUI: descobrir porque o angular nao esta enviando o cookie
      let data$ = this.http.get<IUser>(this.url+'/api/usuario');
      this._user = await firstValueFrom(data$);

      return {
        isOk: true,
        data: this._user
      };
    } catch {
      return {
        isOk: false,
        data: null
      };
    }
  }

  async createAccount(name: string, email: string, password: string) {
    try {
      // Send request
      console.log(email, password);
      let data = {nome: name, email: email, senha: password};
      let data$ = this.http.post<IJwtResponse>('/api/auth/save', data);
      let value = await firstValueFrom(data$);

      this.router.navigate(['/create-account']);
      return {
        isOk: true
      };
    } catch {
      return {
        isOk: false,
        message: "Failed to create account"
      };
    }
  }

  async changePassword(email: string, recoveryCode: string) {
    try {
      // Send request
      console.log(email, recoveryCode);

      return {
        isOk: true
      };
    } catch {
      return {
        isOk: false,
        message: "Failed to change password"
      }
    }
    ;
  }

  async resetPassword(email: string) {
    try {
      // Send request
      console.log(email);

      return {
        isOk: true
      };
    } catch {
      return {
        isOk: false,
        message: "Failed to reset password"
      };
    }
  }

  async logOut() {

    let data$ = this.http.post<any>(this.url+'/api/auth/logout', {});
    let value = await firstValueFrom(data$);

    this._user = null;
    this.router.navigate(['/login-form']);
  }
}

@Injectable()
export class AuthGuardService implements CanActivate {
  constructor(private router: Router, private authService: AuthService) {
  }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const isLoggedIn = this.authService.loggedIn;
    const isAuthForm = [
      'login-form',
      'reset-password',
      'create-account',
      'change-password/:recoveryCode'
    ].includes(route.routeConfig?.path || defaultPath);

    if (isLoggedIn && isAuthForm) {
      this.authService.lastAuthenticatedPath = defaultPath;
      this.router.navigate([defaultPath]);
      return false;
    }

    if (!isLoggedIn && !isAuthForm) {
      this.router.navigate(['/login-form']);
    }

    if (isLoggedIn) {
      this.authService.lastAuthenticatedPath = route.routeConfig?.path || defaultPath;
    }

    return isLoggedIn || isAuthForm;
  }

}


