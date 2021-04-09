import { OnDestroy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Skill } from '../common/model/skill';
import { SkillService } from '../common/services/skill.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit, OnDestroy {

  skills: Skill[] = [];
  private subscriptions: Subscription[] = [];
  
  constructor(private skillService: SkillService, private router: Router) { }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      s.unsubscribe();
    });
  }

  ngOnInit(): void {
    this.getSkills();
  }

  private getSkills(): void {
    this.subscriptions.push(this.skillService.getSkillsDetails().subscribe(
      (response: Skill[]) => {
        this.skills = response.sort((a, b) => (a.enabled ? 1 : -1));        
      },
      (error) => {
        console.log("error", error);
      },
      () => {
        console.log("complete");
        console.log(this.skills);
      }
    ));
  }

}
