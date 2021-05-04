import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../model/user';
import { UserInfo } from '../model/user-info';
import { environment } from 'src/environments/environment';
import { UserRegistration } from '../model/user-registration';
import { UserAuth } from '../model/user-auth';
import { map } from 'rxjs/operators';
import { ACCESS_TOKEN, REFRESH_TOKEN } from '../helper/token.helper';

@Injectable({
  providedIn: 'root'
})
export class UserHttpService {

  constructor(private http: HttpClient) { }

  // tslint:disable-next-line: typedef
  private authHeader() {
    return {
      headers: new HttpHeaders().set('Authorization', `Bearer ${sessionStorage.getItem(ACCESS_TOKEN)}`)
    };
  }

  private authHeaderBlob(): any {
    return {
      responseType: 'blob',
      headers: new HttpHeaders().set('Authorization', `Bearer ${sessionStorage.getItem(ACCESS_TOKEN)}`)
    };
  }

  public getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${environment.url}/admin/users`, this.authHeader());
  }

  public save(user: UserRegistration): Observable<UserRegistration> {
    return this.http.post<UserRegistration>(`${environment.url}/registration`, user);
  }

  public getByEmail(email: string): Observable<any> {
    return this.http.get<User>(`${environment.url}/user/${email}`, this.authHeader());
  }

  public deleteUser(email: string): Observable<any> {
    return this.http.delete<User>(`${environment.url}/user/${email}`, this.authHeader());
  }

  public login(user: UserRegistration): Observable<boolean> {
    return this.http.post<UserAuth>(`${environment.url}/login`, user)
      .pipe(
        map(result => {
          sessionStorage.setItem(ACCESS_TOKEN, result.accessToken);
          sessionStorage.setItem(REFRESH_TOKEN, result.refreshToken);
          return true;
        })
      );
  }

  public updateUserInfo(userInfo: UserInfo): Observable<UserInfo> {
    return this.http.put<UserInfo>(`${environment.url}/user-profile/`, userInfo, this.authHeader());
  }

  public loadImage(imageURL: string): Observable<any> {
    return this.http.get(`${environment.url}/user-profile/load-photo/${imageURL}`,  this.authHeaderBlob());
  }

  public saveImage(photo: any, id: string, maxPhotoLength: BigInteger): Observable<Blob> {
    return this.http.put<any>(`${environment.url}/user-profile/save-photo/${id}/${maxPhotoLength}`, photo, this.authHeader());
  }

  public loggedIn(): void {
    if (sessionStorage.getItem(ACCESS_TOKEN)) {
      sessionStorage.removeItem(ACCESS_TOKEN);
    }
    if (sessionStorage.getItem(REFRESH_TOKEN)) {
      sessionStorage.removeItem(REFRESH_TOKEN);
    }
  }

  public refreshToken(userAuth: UserAuth): Observable<UserAuth> {
    return this.http.post<UserAuth>(`${environment.url}/refresh-token`, userAuth);
  }

  public checkRegistrationToken(token: string): Observable<boolean> {
    return this.http.get<boolean>(`${environment.url}/verify-email/${token}`);
  }
}
