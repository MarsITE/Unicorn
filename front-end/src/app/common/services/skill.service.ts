import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Skill } from '../model/skill';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SkillService {

  constructor(private http: HttpClient) { }

  public getSkills(): Observable<Skill[]> {    
    return this.http.get<Skill[]>(`${environment.url}/skills`);
  }
}
