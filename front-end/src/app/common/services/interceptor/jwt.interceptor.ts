import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserHttpService } from '../user-http.service';
import { TokenService } from '../token.service';
import { error } from 'selenium-webdriver';
import { UserAuth } from '../../model/user-auth';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private userService: UserHttpService, private tokenService: TokenService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (!this.tokenService.isValidToken()) {
      const userAuth: UserAuth = {
        email: this.tokenService.getEmailFromToken(),
        accessToken: '',
        refreshToken: sessionStorage.getItem('refresh_token')};
      const subscription = this.userService.refreshToken(userAuth)
      .subscribe(
        result => {
          sessionStorage.setItem('access_token', result.accessToken);
          sessionStorage.setItem('refresh-token', result.refreshToken);
        },
        error => console.log(error),
      );
    }
    return next.handle(request);
  }
}
