import { Injectable } from '@angular/core';

@Injectable()
export class AppInfoService {
  constructor() {}

  public get title() {
    return 'LoginAngular';
  }

  public get currentYear() {
    return new Date().getFullYear();
  }
// AQUI: guardar o ultimo usuario e senha logado
  saveSessionUser(usuario: string, senha: string) {
    sessionStorage.setItem("usuario", usuario);
    sessionStorage.setItem("senha", senha);
  }

  getSessionStorage(name: string) {
    return sessionStorage.getItem(name);
  }

  removeSessionStorage(name: string) {
    sessionStorage.removeItem(name);
  }
}
