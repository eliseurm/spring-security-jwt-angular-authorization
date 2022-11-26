import { Injectable } from '@angular/core';
import {User} from "../data/user";
import * as CryptoJS from 'crypto-js';

@Injectable()
export class AppInfoService {
  constructor() {}

  public get title() {
    return 'LoginAngular';
  }

  public get currentYear() {
    return new Date().getFullYear();
  }

  // --- guardo o "user" logado
  saveStorageUser(user: User) {
    localStorage.setItem("user", JSON.stringify(user));
  }

  getStorageUser() {
    let user: User | null = null;
    let userStr: string | null = localStorage.getItem("user");

    if (userStr) {
      user = JSON.parse(userStr) as User;
    }

    return user;
  }

  removeStorageUser() {
    localStorage.removeItem("user");
  }

  // --- Guardar usuario e senha
  saveStorageUsuarioSenha(usuario: string, senha: string){
    senha = CryptoJS.AES.encrypt(senha, 'keyEncriptUser').toString();
    let storage = {
      usuario: usuario,
      senha: senha
    }
    localStorage.setItem("guardarUsuario", JSON.stringify(storage));
  }

  getStorageUsuarioSenha() {
    let obj;
    let str: string | null = localStorage.getItem("guardarUsuario");

    if (str) {
      obj = JSON.parse(str);
      let bytes  = CryptoJS.AES.decrypt(obj.senha, 'keyEncriptUser');
      obj.senha = bytes.toString(CryptoJS.enc.Utf8);
    }

    return obj;
  }

  removeStorageUsuarioSenha() {
    localStorage.removeItem("guardarUsuario");
  }

}
