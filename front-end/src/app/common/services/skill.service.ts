import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Skill } from '../model/skill';
import { environment } from 'src/environments/environment';
import { ACCESS_TOKEN } from '../helper/token.helper';

@Injectable({
  providedIn: 'root'
})
export class SkillService {
  constructor(private http: HttpClient) { }

  // tslint:disable-next-line: typedef
  private authHeader() {
    return {
      headers: new HttpHeaders().set('Authorization', `Bearer ${sessionStorage.getItem(ACCESS_TOKEN)}`)
    };
  }

  // for Search
  public getSkills(): Observable<Skill[]> {
    return this.http.get<Skill[]>(`${environment.url}/skills`);
  }

  // for Workers
  public getWorkerSkills(): Observable<Skill[]> {
    return this.http.get<Skill[]>(`${environment.url}/worker/skills`, this.authHeader());
  }

  public addWorkerSkill(skill: Skill): Observable<Skill> {
    return this.http.post<Skill>(`${environment.url}/worker/skills`, skill, this.authHeader()); 
  }

  // for Admin
  public getSkillsDetails(): Observable<Skill[]> {
    return this.http.get<Skill[]>(`${environment.url}/admin/skills`, this.authHeader());
  }
  
  public save(skill: Skill): Observable<Skill> {
    return this.http.post<Skill>(`${environment.url}/admin/skills`, skill, this.authHeader()); 
  }

  public update(skill: Skill): Observable<Skill> {
    return this.http.put<Skill>(`${environment.url}/admin/skills`, skill, this.authHeader());
  }
}
