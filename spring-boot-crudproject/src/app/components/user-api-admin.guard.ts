import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuthServiceComponent } from './services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserApiAdminGuard implements CanActivate {
  constructor(private router: Router, private auth: AuthServiceComponent) { }

  canActivate(): boolean {
    if (!this.auth.isLoggedIn()) {
      this.router.navigate(['login/user']);
      return false;
    }
    return true;
  }
}
