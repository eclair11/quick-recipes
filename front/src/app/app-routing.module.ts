import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { ListComponent } from './recipe/list/list.component';
import { DisplayComponent } from './recipe/display/display.component';
import { InscriptionComponent } from './user/inscription/inscription.component';
import { ConnectionComponent } from './user/connection/connection.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'recipe/list', component: ListComponent },
  { path: 'recipe/display', component: DisplayComponent },
  { path: 'user/inscription', component: InscriptionComponent },
  { path: 'user/connection', component: ConnectionComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }