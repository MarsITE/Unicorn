import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ACCESS_TOKEN } from 'src/app/constants';
import { environment } from 'src/environments/environment';
import { Project } from '../model/project';
import {WorkerProject} from '../model/worker-project';


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
  private par(counter: { toString: () => string; }, sort: string, maxResult: { toString: () => string; }): any {
    return new HttpParams()
    .set('page', counter.toString())
    .set('sort', sort)
    .set('maxResult', maxResult.toString());
  }
  private searchCountParam(_skillList: String[]):any {
    return new HttpParams()
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

  public getWorkerProjects(counter: string, sort: string): Observable<WorkerProject[]> {
    let params = new HttpParams()
      .set('page', counter.toString())
      .set('sort', sort);
    const options = {
      params: params
    };
    if (sessionStorage.getItem(ACCESS_TOKEN) !== null) {
      options['headers'] = this.authHeader();
    }
    return this.http.get<WorkerProject[]>(`${environment.url}/workers/projects`, options);
  }

  public getWorkersProjects(workerId: string, counter: string, sort: string): Observable<WorkerProject[]> {
    let params = new HttpParams()
      .set('page', counter.toString())
      .set('sort', sort);
    const options = {
      params: params
    };
    if (sessionStorage.getItem(ACCESS_TOKEN) !== null) {
      options['headers'] = this.authHeader();
    }
    return this.http.get<WorkerProject[]>(`${environment.url}/workers/${workerId}/projects`, options);
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

  public getSearchProjects(counter: string, sort: string, maxResult: string, _skillList: string[]):Observable<Project[]> {
    return this.http.get<Project[]>(`${environment.url}/projects/search`, {
      params: this.paginationParams(counter, sort, maxResult, _skillList)
    });
  }

  public getProjectsByUserSkills(counter: string, sort: string, maxResult: string):Observable<Project[]> {
    return this.http.get<Project[]>(`${environment.url}/projects/worker`, {
      params: this.par(counter, sort, maxResult),
      headers: this.authHeader()
    });
  }

  public save(project: Project): Observable<Project> {
    return this.http.post<Project>(`${environment.url}/projects`, project, { headers: this.authHeader() });
  }

  public update(projectId: string, project: Project): Observable<Project> {
    return this.http.put<Project>(`${environment.url}/projects/${projectId}`, project, { headers: this.authHeader() });
  }

  public deleteProject(id: string): Observable<any> {
    return this.http.delete<Project>(`${environment.url}/projects/${id}`, { headers: this.authHeader() });
  }

  public getById(id: string): Observable<Project> {
    return this.http.get<Project>(`${environment.url}/projects/${id}`, { headers: this.authHeader() });
  }

  public getAllProjectsCount(): Observable<number> {
    return this.http.get<number>(`${environment.url}/projects/count/all`);
  }

  public getAllProjectsCountBySkills(): Observable<number> {
    return this.http.get<number>(`${environment.url}/projects/count/user/skills`, {
      headers: this.authHeader()
    });
  }

  public getAllProjectsCountBySearchSkills(_skillList: string[]): Observable<number> {
    return this.http.get<number>(`${environment.url}/projects/count/search`, {
      params: this.searchCountParam(_skillList),
      headers: this.authHeader()
    });
  }

  public joinProject(projectId: string) {
    return this.http.post<Project>(`${environment.url}/projects/${projectId}/workers`, {}, { headers: this.authHeader() });
  }
}

