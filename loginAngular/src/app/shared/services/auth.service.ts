import {Injectable, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {IUser} from "../data/IUser";
import {HttpClient} from "@angular/common/http";
import {firstValueFrom} from "rxjs";
import {IJwtResponse} from "../data/IJwtResponse";
import {Usuario} from "../data/usuario";
import {AppInfoService} from "./app-info.service";

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

  get user(){
    return this._user;
  }

  set lastAuthenticatedPath(value: string) {
    this._lastAuthenticatedPath = value;
  }

  constructor(private router: Router,
              private appInfoService: AppInfoService,
              private http: HttpClient) {

    this._user = this.appInfoService.getStorageUser();

  }


  async logIn(email: string, password: string) {
    // let headers = new HttpHeaders().set('Content-Type', 'application/json');
    let data = {nome: email, senha: password};

    try {
      // Send request
      let data$ = this.http.post<IJwtResponse>(this._url+'/api/auth/login', data);
      let value = await firstValueFrom(data$);

      let usuario: Usuario = await this.getUser();

      this._user = new class implements IUser {
        avatarUrl: string = "https://js.devexpress.com/Demos/WidgetsGallery/JSDemos/images/employees/06.png";
        email: string = usuario.email;
        token: string = "";
      }

      this.appInfoService.saveStorageUser(this._user);
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

  async getUser(): Promise<Usuario> {
      let data$ = this.http.get<Usuario>(this._url+'/api/usuario');
      return await firstValueFrom(data$);
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
    await firstValueFrom(data$);

    this._user = null;
    this.appInfoService.removeStorageUser();

    this.router.navigate(['/login-form']);
  }
}


