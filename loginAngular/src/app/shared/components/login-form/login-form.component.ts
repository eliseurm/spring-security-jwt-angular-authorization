import { CommonModule } from '@angular/common';
import { Component, NgModule } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { DxFormModule } from 'devextreme-angular/ui/form';
import { DxLoadIndicatorModule } from 'devextreme-angular/ui/load-indicator';
import notify from 'devextreme/ui/notify';
import {AppInfoService, AuthService} from '../../services';


@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent {
  loading = false;
  formData: any = {};

  constructor(private authService: AuthService,
              private router: Router,
              private appInfoService: AppInfoService) {

    let storage = this.appInfoService.getStorageUsuarioSenha();
    if(storage){
      this.formData = {
        email: storage.usuario,
        password: storage.senha,
        rememberMe: true
      }
    }
  }

  async onSubmit(e: Event) {
    e.preventDefault();
    const { email, password, rememberMe } = this.formData;
    this.loading = true;

    if(rememberMe){
      this.appInfoService.saveStorageUsuarioSenha(email, password);
    }
    else{
      this.appInfoService.removeStorageUsuarioSenha();
    }

    const result = await this.authService.logIn(email, password);
    if (!result.isOk) {
      notify(result.message, 'error', 2000);
    }
    this.loading = false;
  }

  onCreateAccountClick = () => {
    this.router.navigate(['/create-account']);
  }

}

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    DxFormModule,
    DxLoadIndicatorModule
  ],
  declarations: [ LoginFormComponent ],
  exports: [ LoginFormComponent ]
})
export class LoginFormModule { }
