import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ACCESS_TOKEN, REFRESH_TOKEN, TokenHelper } from '../../helper/token.helper';
import { UserAuth } from '../../model/user-auth';
import { UserHttpService } from '../user-http.service';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private router: Router, private userService: UserHttpService, private tokenHelper: TokenHelper) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.updateAccessToken();
    return next.handle(request)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          const errorMsg = error.error.message;
          switch (error.status) {
            case 401:
              this.router.navigateByUrl('login');
              break;
            case 403:
              this.router.navigateByUrl('not-found');
              break;
            default:
              break;
          }
          return throwError(errorMsg);
        }));

  }

  private updateAccessToken(): void {
    if (sessionStorage.getItem(ACCESS_TOKEN) != null) {
      if (!this.tokenHelper.isValidToken()) {
        const userAuth: UserAuth = {
          email: this.tokenHelper.getEmailFromToken(),
          accessToken: '',
          refreshToken: sessionStorage.getItem(REFRESH_TOKEN)
        };
        this.userService.refreshToken(userAuth)
          .subscribe(
            result => {
              sessionStorage.setItem(ACCESS_TOKEN, result.accessToken);
              sessionStorage.setItem(REFRESH_TOKEN, result.refreshToken);
            },
            error => console.log(error),
          );
      }
    }
  }
}
