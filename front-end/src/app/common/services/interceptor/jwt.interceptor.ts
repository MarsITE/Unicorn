import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserHttpService } from '../user-http.service';
import { UserAuth } from '../../model/user-auth';
import { TokenHelper } from '../../helper/token.helper';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private userService: UserHttpService, private tokenHelper: TokenHelper) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (!this.tokenHelper.isValidToken()) {
      const userAuth: UserAuth = {
        email: this.tokenHelper.getEmailFromToken(),
        accessToken: '',
        refreshToken: sessionStorage.getItem('refresh_token')};
      this.userService.refreshToken(userAuth)
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
