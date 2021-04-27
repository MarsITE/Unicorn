import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Project } from '../model/project';
import { Skill } from '../model/skill';
import { environment } from 'src/environments/environment';
import { ACCESS_TOKEN } from '../helper/token.helper';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  constructor(private http: HttpClient) { }

  // tslint:disable-next-line: typedef
  private authHeader() {
    return new HttpHeaders().set('Authorization', `Bearer ${sessionStorage.getItem(ACCESS_TOKEN)}`);
  }


  private params(counter: { toString: () => string; }, sort: string,
                 maxResult: { toString: () => string; }, showAll: boolean): any {
    return new HttpParams()
    .set('page', counter.toString())
    .set('sort', sort)
    .set('showAll', showAll.toString())
    .set('maxResult', maxResult.toString());
  }

  private paginationParams(counter: { toString: () => string; }, sort: string, maxResult: { toString: () => string; }, _skillList: String[]): any {
    return new HttpParams()
    .set('page', counter.toString())
    .set('sort', sort)
    .set('maxResult', maxResult.toString())
    .set('skillList', _skillList.toString());
  }

  public getProjects(counter: string, sort: string, maxResult: string, showAll: boolean = true): Observable<Project[]> {
    const options = {
      params: this.params(counter, sort, maxResult, showAll)
    };
    if (sessionStorage.getItem(ACCESS_TOKEN) !== null) {
      options['headers'] = this.authHeader();
    }
    return this.http.get<Project[]>(`${environment.url}/projects`, options);
  }

  public getAllProjects(counter: string, sort: string, maxResult: string, showAll: boolean = true): Observable<Project[]> {
    return this.http.get<Project[]>(`${environment.url}/all-projects`, {
      params: this.params(counter, sort, maxResult, showAll),
      headers: this.authHeader()
    }
    );
  }

  public getProjectsById(counter: string, sort: string, maxResult: string): Observable<Project[]> {
    return this.http.get<Project[]>(`${environment.url}/projects`, {
      params: this.params(counter, sort, maxResult, true),
      headers: this.authHeader()
    }
    );
  }

  public getSearchProjects(counter: string, sort: string, maxResult: string, _skillList: String[]):Observable<Project[]> {
    return this.http.get<Project[]>(`${environment.url}/projects/search`, {
      params: this.paginationParams(counter, sort, maxResult, _skillList)
    });
  }

  public save(project: Project): Observable<Project> {
    return this.http.post<Project>(`${environment.url}/projects`, project, { headers: this.authHeader() });
  }

  public update(project: Project): Observable<Project> {
    return this.http.put<Project>(`${environment.url}/projects`, project, { headers: this.authHeader() });
  }

  public deleteProject(id: string): Observable<any> {
    return this.http.delete<Project>(`${environment.url}/projects/${id}`, { headers: this.authHeader() });
  }

  public getById(id: string): Observable<Project> {
    return this.http.get<Project>(`${environment.url}/projects/${id}`, { headers: this.authHeader() });
  }
}

