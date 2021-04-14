import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserHttpService } from './user-http.service';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  helper: JwtHelperService;
  token = sessionStorage.getItem('access_token');

  constructor(private userService: UserHttpService) {
    this.helper = new JwtHelperService();
  }

  public isValidToken(): boolean {
    return this.token !== null && this.helper.isTokenExpired(this.token) ? true : false ;
  }

  public getIdFromToken(): string {
    return this.getTokenData().id;
  }

  public getEmailFromToken(): string {
    return this.getTokenData().sub;
  }

  private getTokenData(): any {
    return this.helper.decodeToken(this.token);
  }

  private refreshToken(): any {
    if (!this.helper.isTokenExpired(this.token)) {

    }
  }

  public getRoles(): any {
    return this.getTokenData().role;
  }
}
