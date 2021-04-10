import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Skill } from '../model/skill';
import { environment } from 'src/environments/environment';
import { StorageService } from './storage.service';

@Injectable({
  providedIn: 'root'
})
export class SkillService {

  constructor(private http: HttpClient, private storageService: StorageService) { }

  // tslint:disable-next-line: typedef
  private authHeader() {
    return {
      headers: new HttpHeaders().set('Authorization', `Bearer ${this.storageService.getValue('token')}`)
    };
  }

  // for Users
  public getSkills(): Observable<Skill[]> {    
    
    return this.http.get<Skill[]>(`${environment.url}/skills`);
  }

  // for Admin
  public getSkillsDetails(): Observable<Skill[]> {    
    return this.http.get<Skill[]>(`${environment.url}/admin/skills`);
  }
}
