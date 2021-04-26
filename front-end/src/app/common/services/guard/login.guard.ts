import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenHelper, ACCESS_TOKEN, REFRESH_TOKEN } from '../../helper/token.helper';
import { UserAuth } from '../../model/user-auth';
import { UserHttpService } from '../user-http.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {

  constructor(private router: Router, private tokenHelper: TokenHelper) {}

  canActivate(): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (this.tokenHelper.isValidToken) {
      return true;
    }
    this.router.navigateByUrl('login');
    return false;
  }

}
