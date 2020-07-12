import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { SignComponent } from './user/sign/sign.component';
import { ListComponent } from './recipe/list/list.component';
import { DisplayComponent } from './recipe/display/display.component';
import { MessageComponent } from './message/message.component';

import { HttpInterceptorService } from './user/service/http-interceptor.service';
import { UserService } from './user/service/user.service';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    SignComponent,
    ListComponent,
    DisplayComponent,
    MessageComponent
  ],
  imports: [
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatTabsModule,
    MatCardModule,
    MatListModule
  ],
  providers: [
    UserService,
    {
      provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorService, multi: true
    }
  ],
  bootstrap: [AppComponent]
})

export class AppModule { }