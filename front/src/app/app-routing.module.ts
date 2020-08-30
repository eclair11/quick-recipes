import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { SignComponent } from './user/sign/sign.component';
import { FavoriteComponent } from './user/favorite/favorite.component';
import { ListComponent } from './recipe/list/list.component';
import { DisplayComponent } from './recipe/display/display.component';
import { MenuComponent } from './admin/menu/menu.component';
import { AddComponent } from './admin/add/add.component';
import { RemoveComponent } from './admin/remove/remove.component';
import { AuthGuardService } from './user/service/auth-guard.service';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'user/sign', component: SignComponent, canActivate: [AuthGuardService] },
  { path: 'user/favs', component: FavoriteComponent, canActivate: [AuthGuardService] },
  { path: 'recipe/list/:type/:key', component: ListComponent },
  { path: 'recipe/display/:id', component: DisplayComponent },
  { path: 'admin/menu', component: MenuComponent, canActivate: [AuthGuardService] },
  { path: 'admin/add', component: AddComponent, canActivate: [AuthGuardService] },
  { path: 'admin/remove', component: RemoveComponent, canActivate: [AuthGuardService] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }