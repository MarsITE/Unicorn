import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../model/user';
import { UserInfo } from '../model/user-info';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserHttpService {

  constructor(private http: HttpClient) { }

  public getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${environment.url}/users`);
  }

  public save(user: User): Observable<User> {
    return this.http.post<User>(`${environment.url}/user`, user);
  }

  public deleteUser(id: string): Observable<any> {
    return this.http.delete<User>(`${environment.url}/user/${id}`);
  }

  public getByEmail(email: string): Observable<User> {
    return this.http.get<User>(`${environment.url}/user/${email}`);
  }

  public updateUserInfo(userInfo: UserInfo): Observable<UserInfo> {
    return this.http.put<UserInfo>(`${environment.url}/user-info/`, userInfo);
  }

  public loadImage(imageURL: string): Observable<any> {
    return this.http.get(`${environment.url}/user-info/loadimage/${imageURL}`, { responseType: 'blob' });
  }
}
