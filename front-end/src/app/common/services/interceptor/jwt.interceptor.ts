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
import { ACCESS_TOKEN, REFRESH_TOKEN, TokenHelper } from '../../helper/token.helper';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private userService: UserHttpService, private tokenHelper: TokenHelper) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (!this.tokenHelper.isValidToken()) {
      console.log(this.tokenHelper.isValidToken);
      console.log(this.tokenHelper.getEmailFromToken);

      const userAuth: UserAuth = {
        email: this.tokenHelper.getEmailFromToken(),
        accessToken: '',
        refreshToken: sessionStorage.getItem(REFRESH_TOKEN)};
      this.userService.refreshToken(userAuth)
      .subscribe(
        result => {
          sessionStorage.setItem(ACCESS_TOKEN, result.accessToken);
          sessionStorage.setItem(REFRESH_TOKEN, result.refreshToken);
        },
        error => console.log(error),
      );
    }
    return next.handle(request);
  }
}
