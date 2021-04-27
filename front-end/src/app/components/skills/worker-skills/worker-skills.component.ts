import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Skill } from '../../../common/model/skill';
import { SkillService } from '../../../common/services/skill.service';
import { ToastrService } from 'ngx-toastr';
import {MatDialog} from '@angular/material/dialog';
import { AddSkillsComponent } from '../add-skills/add-skills.component';

@Component({
  selector: 'app-worker-skills',
  templateUrl: './worker-skills.component.html',
  styleUrls: ['./worker-skills.component.css']
})
export class WorkerSkillsComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];
  skills: Skill[] = [];
  
  selectable = true;
  removable = true;

  constructor(private skillService: SkillService, private toastr: ToastrService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.getWorkerSkills();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      s.unsubscribe();
    });
  }

  getWorkerSkills(): void {
    this.subscriptions.push(this.skillService.getWorkerSkills().subscribe(
      (response: Skill[]) => {
        this.skills = response.sort((a: Skill) => (a.enabled ? 1 : -1));
      },
      (error) => { this.toastr.error('Skills were not received!', 'Error!'); }
    ));
  }

  showModalForm(): void {
    const dialogRef = this.dialog.open(AddSkillsComponent, { width: '600px', data: {} });
    dialogRef.afterClosed().subscribe(result => {      
      if (result.submit) {        
        const skillNames: string[] = result.skills.split(';');
        this.addSkills(skillNames);        
      }
    });
  }

  addSkills(skillNames: string[]): void {
    let skills : Skill[] = [];
    skillNames.forEach(name => skills.push({skillId: '', name}));
     this.skillService.addWorkerSkills(skills)
     .subscribe(data => {
       const message = `Skill has been added successfully`;
      this.toastr.success(message, 'Success!');
    },
    errMessage => { this.toastr.warning(errMessage, 'Warning!'); }
    );  
  }
  
    removeSkill(skill: Skill): void {
      // todo
    }
}
