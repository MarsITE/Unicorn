import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class TokenHelper {
  helper: JwtHelperService;

  constructor() {
    this.helper = new JwtHelperService();
  }

  public isValidToken(): boolean {
    return sessionStorage.getItem('access_token') !== null && this.helper.isTokenExpired(sessionStorage.getItem('access_token'));
  }

  public getIdFromToken(): string {
    return this.getTokenData().id;
  }

  public getEmailFromToken(): string {
    return this.getTokenData().sub;
  }

  private getTokenData(): any {
    return this.helper.decodeToken(sessionStorage.getItem('access_token'));
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
