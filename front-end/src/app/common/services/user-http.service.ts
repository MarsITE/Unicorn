import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ACCESS_TOKEN } from 'src/app/constants';
import { environment } from 'src/environments/environment';
import { User } from '../model/user';
import { UserInfo } from '../model/user-info';
import { UserRegistration } from '../model/user-registration';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class UserHttpService {

  constructor(private http: HttpClient, private authenticationService: AuthenticationService) { }

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

  public get(id: string): Observable<any> {
    return this.http.get<User>(`${environment.url}/user/${id}`, this.authHeader());
  }

  public delete(id: string): Observable<any> {
    return this.http.delete<User>(`${environment.url}/user/${id}`, this.authHeader());
  }

  public updateUserInfo(userInfo: UserInfo): Observable<UserInfo> {
    return this.http.put<UserInfo>(`${environment.url}/user-profile/`, userInfo, this.authHeader());
  }

  public loadImage(imageURL: string): Observable<any> {
    return this.http.get(`${environment.url}/user-profile/load-photo/${imageURL}`, this.authHeaderBlob());
  }

  public saveImage(photo: any, id: string, maxPhotoLength: BigInteger): Observable<Blob> {
    return this.http.put<any>(`${environment.url}/user-profile/save-photo/${id}/${maxPhotoLength}`, photo, this.authHeader());
  }

  public checkRegistrationToken(token: string): Observable<boolean> {
    return this.http.get<boolean>(`${environment.url}/verify-email/${token}`);
  }
}
