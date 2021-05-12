import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ACCESS_TOKEN, REFRESH_TOKEN } from 'src/app/constants';
import { environment } from 'src/environments/environment';
import { UserAuth } from '../model/user-auth';
import { UserRegistration } from '../model/user-registration';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  public user: Observable<UserAuth>;

  private userSubject: BehaviorSubject<UserAuth>;
  private helper: JwtHelperService;

  private refreshTokenTimeout;

  constructor(
    private router: Router,
    private http: HttpClient
  ) {
    this.userSubject = new BehaviorSubject<UserAuth>(null);
    this.user = this.userSubject.asObservable();
    this.helper = new JwtHelperService();

    const accessToken = sessionStorage.getItem(ACCESS_TOKEN);
    const refreshToken = sessionStorage.getItem(REFRESH_TOKEN);
    if (accessToken && refreshToken) {
      this.userSubject.next({
        email: this.helper.decodeToken(accessToken).sub,
        accessToken,
        refreshToken
      });
    }
  }

  public get userValue(): UserAuth {
    return this.userSubject.value;
  }

  public login(user: UserRegistration): Observable<boolean> {
    return this.http.post<UserAuth>(`${environment.url}/login`, user)
      .pipe(map((user: UserAuth) => {
        this.userSubject.next(user);
        sessionStorage.setItem(ACCESS_TOKEN, user.accessToken);
        sessionStorage.setItem(REFRESH_TOKEN, user.refreshToken);
        this.startRefreshTokenTimer();
        return true;
      }));
  }

  logout(): void {
    if (sessionStorage.getItem(ACCESS_TOKEN)) {
      sessionStorage.removeItem(ACCESS_TOKEN);
    }
    if (sessionStorage.getItem(REFRESH_TOKEN)) {
      sessionStorage.removeItem(REFRESH_TOKEN);
    }
    this.stopRefreshTokenTimer();
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }

  public refreshToken(): Observable<UserAuth> {
    const userAuth: UserAuth = {
      email: this.userValue ? this.userValue.email : null,
      accessToken: '',
      refreshToken: this.userValue ? this.userValue.refreshToken : null
    };
    return this.http.post<UserAuth>(`${environment.url}/refresh-token`, userAuth)
      .pipe(map((user: UserAuth) => {
        if (user != null) {
          this.userSubject.next(user);
          sessionStorage.setItem(ACCESS_TOKEN, user.accessToken);
          sessionStorage.setItem(REFRESH_TOKEN, user.refreshToken);
          this.startRefreshTokenTimer();
        }
        return user;
      }));
  }


  public isAccessTokenValid(): boolean {
    return this.userValue != null && this.userValue.accessToken != null && !this.helper.isTokenExpired(this.userValue.accessToken);
  }

  public isRefreshTokenValid(): boolean {
    return this.userValue != null && this.userValue.refreshToken != null;
  }

  public getIdFromToken(): string {
    return this.userValue ? this.getTokenData().id : '';
  }

  public getEmailFromToken(): string {
    return this.userValue ? this.getTokenData().email : '';
  }

  public getTokenData(): any {
    return this.userValue ? this.helper.decodeToken(this.userValue.accessToken) : null;
  }

  public isRoleWorker(): boolean {
    return this.userValue ? this.getTokenData().isWorker : false;
  }

  public isRoleEmployer(): boolean {
    return this.userValue ? this.getTokenData().isEmployer : false;
  }

  public isRoleAdmin(): boolean {
    return this.userValue ? this.getTokenData().isAdmin : false;
  }

  private startRefreshTokenTimer(): void {
    if (this.userValue) {
      const expires = new Date(this.getTokenData().exp * 1000);
      const timeout = expires.getTime() - Date.now() - (60 * 1000);
      this.refreshTokenTimeout = setTimeout(() => this.refreshToken().subscribe(), timeout);
    }
  }

  private stopRefreshTokenTimer(): void {
    clearTimeout(this.refreshTokenTimeout);
  }

}
