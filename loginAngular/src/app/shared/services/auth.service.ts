import {Injectable, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {IUser} from "../data/IUser";
import {HttpClient} from "@angular/common/http";
import {firstValueFrom} from "rxjs";
import {IJwtResponse} from "../data/IJwtResponse";

// const defaultUser = {
//   token: '',
//   email: 'admin',
//   avatarUrl: 'https://js.devexpress.com/Demos/WidgetsGallery/JSDemos/images/employees/06.png'
// };

@Injectable()
export class AuthService {

  private _user: IUser | null = null;
  private _defaultPath = '/';
  private _lastAuthenticatedPath: string = this._defaultPath;
  private _url = "http://localhost:8080";


  get defaultPath(): string{
    return this._defaultPath;
  }

  get loggedIn(): boolean {
    return !!this._user;
  }

  set lastAuthenticatedPath(value: string) {
    this._lastAuthenticatedPath = value;
  }

  constructor(private router: Router,
              // private authService: AuthService,
              private http: HttpClient) {

  }


  async logIn(email: string, password: string) {
    // let headers = new HttpHeaders().set('Content-Type', 'application/json');
    let data = {nome: email, senha: password};

    try {
      // Send request
      let data$ = this.http.post<IJwtResponse>(this._url+'/api/auth/login', data);
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
      let data$ = this.http.get<IUser>(this._url+'/api/usuario');
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

    let data$ = this.http.post<any>(this._url+'/api/auth/logout', {});
    let value = await firstValueFrom(data$);

    this._user = null;
    this.router.navigate(['/login-form']);
  }
}


