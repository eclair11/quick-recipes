import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { SignComponent } from './user/sign/sign.component';
import { ListComponent } from './recipe/list/list.component';
import { DisplayComponent } from './recipe/display/display.component';

import { AuthGuardService } from './user/service/auth-guard.service';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'user/sign', component: SignComponent },
  { path: 'recipe/list/:type/:key', component: ListComponent },
  { path: 'recipe/display/:id', component: DisplayComponent, canActivate: [AuthGuardService] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }