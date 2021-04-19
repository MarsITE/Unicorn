import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private router: Router) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
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
}
