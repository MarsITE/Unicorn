import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Observable } from 'rxjs';
import { User } from '../model/user';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserHttpService {

  constructor(private http: HttpClient) { }

  public getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${environment.url}/users/all`);
  }

  public save(user: User): Observable<User> {
    return this.http.post<User>(`${environment.url}/users/create`, user);
  }

  public deleteUser(id: string): Observable<any> {
    return this.http.delete<User>(`${environment.url}/users/${id}`);
  }

  public getById(id: string): Observable<User> {
    return this.http.get<User>(`${environment.url}/users/${id}`);
  }
}
