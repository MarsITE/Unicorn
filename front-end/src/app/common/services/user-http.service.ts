import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../model/user';
import { UserInfo } from '../model/user-info';
import { environment } from 'src/environments/environment';
import { UserRegistration } from '../model/user-registration';
import { UserAuth } from '../model/user-auth';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserHttpService {

  constructor(private http: HttpClient) { }

  // tslint:disable-next-line: typedef
  private authHeader() {
    return {
      headers: new HttpHeaders().set('Authorization', `Bearer ${localStorage.getItem('access_token')}`)
    };
  }

  private authHeaderBlob(): any {
    return {
      responseType: 'blob',
      headers: new HttpHeaders().set('Authorization', `Bearer ${localStorage.getItem('access_token')}`)
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
        localStorage.setItem('access_token', result.token);
        return true;
      })
    );
  }

  public updateUserInfo(userInfo: UserInfo): Observable<UserInfo> {
    return this.http.put<UserInfo>(`${environment.url}/user-profile/`, userInfo, this.authHeader());
  }

  public loadImage(imageURL: string): Observable<any> {
    return this.http.get(`${environment.url}/user-profile/load-photo/${imageURL}`, this.authHeaderBlob());
  }

  public saveImage(photo: any, id: string): Observable<Blob> {
    return this.http.put<any>(`${environment.url}/user-profile/save-photo/${id}`, photo, this.authHeader());
  }

  public logout(): void {
    localStorage.removeItem('access_token');
  }

  public get loggedIn(): boolean {
    return (localStorage.getItem('access_token') !== null);
  }
}
