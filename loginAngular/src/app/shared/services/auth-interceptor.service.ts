import { Injectable } from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";

/**
 * Classe abaixo responsável por capturar o token que esta armazenado no browser e enviar no cabeçalho em todas as requisições feitas no backend.
 */
@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor{

  constructor(private authService: AuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add authorization header with jwt token if available
    let request = req;
    let currentUser: any = this.authService.getUser();

    if (currentUser && currentUser.token) {
      request = req.clone({setHeaders: {Authorization: `Bearer ${currentUser.data.token}`}});
    }

    console.log(request);
    return next.handle(request);
  }

}
