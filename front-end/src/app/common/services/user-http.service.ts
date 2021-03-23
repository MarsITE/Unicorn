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
    return this.http.get<User[]>(`${environment.url}/api/users`)
  }

  public save(user: User): Observable<User> {
    return this.http.post<User>(`${environment.url}/api/user`, user);
  }

  public deleteUser(id: string): Observable<any> {
    return this.http.delete<User>(`${environment.url}/api/user/${id}`);
  }

  public getByEmail(email: string): Observable<User> {
    return this.http.get<User>(`${environment.url}/api/user/${email}`);
  }
}
