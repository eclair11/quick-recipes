import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';

import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(private router: Router, private authService: UserService) { }

  canActivate(route: ActivatedRouteSnapshot) {
    let comp = route.routeConfig.component.name;
    if ((comp == "MenuComponent" || comp == "AddComponent" || comp == "RemoveComponent") && !this.authService.admincheck()) {
      this.router.navigate(['/home']);
      return false;
    }
    else if (comp == "SignComponent" && this.authService.signcheck()) {
      this.router.navigate(['/home']);
      return false;
    }
    else if (comp == "SignComponent" && !this.authService.signcheck()) {
      return true;
    }
    else if (!this.authService.signcheck()) {
      this.router.navigate(['user/sign']);
      return false;
    }
    return true;
  }

}