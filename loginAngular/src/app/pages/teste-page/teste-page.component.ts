import { Component, OnInit } from '@angular/core';
import {User} from "../../shared/data/user";
import {AuthService} from "../../shared/services";
import {UsuarioService} from "../../service/usuario.service";

@Component({
  selector: 'app-teste-page',
  templateUrl: './teste-page.component.html',
  styleUrls: ['./teste-page.component.scss']
})
export class TestePageComponent implements OnInit {

  testes: User[] = [];

  constructor(private authService: AuthService,
              private usuarioService: UsuarioService) { }

  ngOnInit(): void {
  }

/*
  async onClickButton(event: any) {
      let ret = await this.authService.logIn("admin", "1234");
      console.log(ret);
  }
*/

  onClickButton(event: any) {
      this.usuarioService.getById(1).subscribe(x => {
        console.log(x);

      });
  }

}
