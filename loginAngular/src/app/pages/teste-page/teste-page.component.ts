import { Component, OnInit } from '@angular/core';
import {IUser} from "../../shared/data/IUser";
import {AuthService} from "../../shared/services";

@Component({
  selector: 'app-teste-page',
  templateUrl: './teste-page.component.html',
  styleUrls: ['./teste-page.component.scss']
})
export class TestePageComponent implements OnInit {

  testes: IUser[] = [];

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
  }

  async onClickButton(event: any) {
      let ret = await this.authService.logIn("admin", "1234");
      console.log(ret);
  }

}
