import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class TokenHelper {
  helper: JwtHelperService;
  token = sessionStorage.getItem('access_token');

  constructor() {
    this.helper = new JwtHelperService();
  }

  public isValidToken(): boolean {
    return this.token !== null && this.helper.isTokenExpired(this.token);
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

  public getRoles(): string[] {
    const roles: string[] = [];
    if (this.getTokenData().isAdmin) {
      roles.push(this.getTokenData().name);
    }
    if (this.getTokenData().isWorker) {
      roles.push(this.getTokenData().name);
    }
    if (this.getTokenData().isEmployer) {
      roles.push(this.getTokenData().name);
    }
    return roles;
  }
}
