import { Component, OnInit } from '@angular/core';
import {IUser} from "../../shared/data/IUser";

@Component({
  selector: 'app-teste-page',
  templateUrl: './teste-page.component.html',
  styleUrls: ['./teste-page.component.scss']
})
export class TestePageComponent implements OnInit {

  testes: IUser[] = [];

  constructor() { }

  ngOnInit(): void {
  }

}
