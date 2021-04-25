import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

export const ACCESS_TOKEN = 'access_token';
export const REFRESH_TOKEN = 'refresh_token';
export const USER_ROLE_ADMIN = 'ADMIN';
export const USER_ROLE_WORKER = 'WORKER';
export const USER_ROLE_EMPLOYER = 'EMPLOYER';

@Injectable({
  providedIn: 'root'
})
export class TokenHelper {
  helper: JwtHelperService;

  constructor() {
    this.helper = new JwtHelperService();
  }

  public isValidToken(): boolean {
    return sessionStorage.getItem(ACCESS_TOKEN) !== null && !this.helper.isTokenExpired(sessionStorage.getItem(ACCESS_TOKEN));
  }

  public getIdFromToken(): string {
    return this.getTokenData().id;
  }

  public getEmailFromToken(): string {
    return this.getTokenData().sub;
  }

  private getTokenData(): any {
    return this.helper.decodeToken(sessionStorage.getItem(ACCESS_TOKEN));
  }

  public isUserRole(userRole: string): boolean {
    const tokenData: { isAdmin?, isEmployer?, isWorker?} = this.getTokenData() || {};    
    const roleNames = [tokenData.isAdmin?.name, tokenData.isEmployer?.name, tokenData.isWorker?.name];    
    return roleNames.some(role => role && role.toUpperCase() === userRole.toUpperCase());
  }
}
