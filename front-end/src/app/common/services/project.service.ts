import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http'
import { Observable } from 'rxjs';
import { Project } from '../model/project';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  constructor(private http: HttpClient) { }

  // tslint:disable-next-line: typedef
  private authHeader() {
    return new  HttpHeaders().set('Authorization', `Bearer ${localStorage.getItem('token')}`);
  }

  private authHeaderWithParams(counter, sort, maxResult): any {
    return new HttpParams()
    .set('page', counter.toString())
    .set('sort', sort)
    .set('maxResult', maxResult.toString());
  }

  public getProjects(counter, sort, maxResult): Observable<Project[]> {
    return this.http.get<Project[]>(`${environment.url}/projects`,
    {
      params: this.authHeaderWithParams(counter, sort, maxResult),
      headers: this.authHeader()
    }
    );
  }

  public save(project: Project): Observable<Project> {
    return this.http.post<Project>(`${environment.url}/projects`, project, {headers: this.authHeader()});
  }

  public deleteProject(id: string): Observable<any> {
    return this.http.delete<Project>(`${environment.url}/projects/${id}`, {headers: this.authHeader()});
  }

  public getById(id: string): Observable<Project> {
    return this.http.get<Project>(`${environment.url}/projects/${id}`, {headers: this.authHeader()});
  }
}

