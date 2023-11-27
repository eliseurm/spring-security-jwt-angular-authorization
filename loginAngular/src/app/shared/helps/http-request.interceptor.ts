import {Injectable} from "@angular/core";
import {HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";


/**
 * Intercepto re seta o parametro da requisicao com withCredentials=true
 * este parametro é responsavel por (???? nao me lembro)
 */

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    req = req.clone({withCredentials: true,});

    return next.handle(req);
    // return next
    //   .handle(req)
    //   .do((ev: HttpEvent<any>) => {
    //     console.log("got an event",ev)
    //     if (ev instanceof HttpResponse) {
    //       console.log('event of type response', ev);
    //     }
    //   });

  }
}

export const httpInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true },
];
