import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Observable } from 'rxjs';
import { Project } from '../model/project';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  constructor(private http: HttpClient) { }

  public getProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(`${environment.url}/projects`);
  }

  public save(project: Project): Observable<Project> {
    return this.http.post<Project>(`${environment.url}/projects`, project);
  }

  public deleteProject(id: string): Observable<any> {
    return this.http.delete<Project>(`${environment.url}/projects/${id}`);
  }

  public getById(id: string): Observable<Project> {
    return this.http.get<Project>(`${environment.url}/projects/${id}`);
  }
}

