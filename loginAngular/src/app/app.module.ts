import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { SideNavOuterToolbarModule, SideNavInnerToolbarModule, SingleCardModule } from './layouts';
import { FooterModule, ResetPasswordFormModule, CreateAccountFormModule, ChangePasswordFormModule, LoginFormModule } from './shared/components';
import { AuthService, ScreenService, AppInfoService } from './shared/services';
import { UnauthenticatedContentModule } from './unauthenticated-content';
import { AppRoutingModule } from './app-routing.module';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import { TestePageComponent } from './pages/teste-page/teste-page.component';
import {DxButtonModule, DxDataGridModule} from "devextreme-angular";

@NgModule({
  declarations: [
    AppComponent,
    TestePageComponent
  ],
  imports: [
    BrowserModule,
    SideNavOuterToolbarModule,
    SideNavInnerToolbarModule,
    SingleCardModule,
    FooterModule,
    ResetPasswordFormModule,
    CreateAccountFormModule,
    ChangePasswordFormModule,
    LoginFormModule,
    UnauthenticatedContentModule,
    AppRoutingModule,
    HttpClientModule,
    DxDataGridModule,
    DxButtonModule
  ],
  providers: [
    AuthService,
    ScreenService,
    AppInfoService,
    HttpClient
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
