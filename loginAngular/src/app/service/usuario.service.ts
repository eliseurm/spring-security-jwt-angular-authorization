import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Usuario} from "../shared/data/usuario";

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private _urlPrefix: string = "http://localhost:8080/api/usuario";

  constructor(private http: HttpClient) { }


  getById(id: any): Observable<Usuario> {
    return this.http.get<Usuario>(`${this._urlPrefix}/${id}`);
  }

/*
  list(): Observable<Usuario[]> {
    return this.http.post<Usuario[]>(`${this._urlPrefix}/list` );
  }
*/

  save(data: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(this._urlPrefix, data)
  }

  update(data: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>(this._urlPrefix, data)
  }

/*
  delete(data: Usuario): Observable<any> {
    // return this.http.delete<Usuario>(`${this._urlPrefix}/${data}`)
    return this.http.delete(this._urlPrefix)
  }
*/


/*
  merge(data: Usuario): Observable<Usuario> {
    if (ObjectUtils.isNullOrUndefined(data.getId())) {
      return this.save(data);
    } else {
      return this.update(data);
    }
  }
*/


}
