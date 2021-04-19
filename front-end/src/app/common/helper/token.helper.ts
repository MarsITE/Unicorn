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
    for (const prop in ['isAdmin', 'isWorker', 'isEmployer']) {
      if (prop in this.getTokenData()) {
        roles.push(this.getTokenData()[prop].name);
      }
    }
    return roles;
  }

  public isCurrentUserAdmin(): boolean {
    const tokenData: {isAdmin?: {name?: string}} = this.getTokenData();
    return tokenData.isAdmin?.name.toUpperCase() === 'ADMIN';
  }
}
