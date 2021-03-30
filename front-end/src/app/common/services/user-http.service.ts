import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../model/user';
import { UserInfo } from '../model/user-info';
import { environment } from 'src/environments/environment';
import { UserAuth } from '../model/user-auth';

@Injectable({
  providedIn: 'root'
})
export class UserHttpService {

  constructor(private http: HttpClient) { }

  public getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${environment.url}/users`);
  }

  public save(user: UserAuth): Observable<UserAuth> {
    return this.http.post<UserAuth>(`${environment.url}/registration`, user);
  }

  public deleteUser(email: string): Observable<any> {
    return this.http.delete<User>(`${environment.url}/user/${email}`);
  }

  public login(email: string, password: string): Observable<UserAuth> {
    const params = new HttpParams()
      .set('email', email)
      .set('password', password);
    return this.http.get<UserAuth>(`${environment.url}/login`, {params});
  }

  public updateUserInfo(userInfo: UserInfo): Observable<UserInfo> {
    return this.http.put<UserInfo>(`${environment.url}/user-profile/`, userInfo);
  }

  public loadImage(imageURL: string): Observable<any> {
    return this.http.get(`${environment.url}/user-profile/load-photo/${imageURL}`, { responseType: 'blob' });
  }

  public saveImage(photo: any, id: string): Observable<Blob> {
    return this.http.put<any>(`${environment.url}/user-profile/save-photo/${id}`, photo);
  }
}
