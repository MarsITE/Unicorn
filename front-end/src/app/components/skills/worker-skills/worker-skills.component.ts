import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Skill } from '../../../common/model/skill';
import { SkillService } from '../../../common/services/skill.service';
import { ToastrService } from 'ngx-toastr';
import {MatDialog} from '@angular/material/dialog';
import { AddSkillsComponent } from '../add-skills/add-skills.component';
import { ConfirmComponent } from '../../modals/confirm/confirm.component';

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
        this.skills = response.sort((a: Skill) => (a.enabled ? -1 : 1));
      },
      (error) => { this.toastr.error('Skills were not received!', 'Error!'); }
    ));
  }

  showModalForm(): void {
    const dialogRef = this.dialog.open(AddSkillsComponent, { width: '600px', data: {} });
    dialogRef.afterClosed().subscribe(result => {      
      if (result.submit) {        
        const skillNames: string[] = result.skills
                                          .split(';')
                                          .map(skillName => skillName.trim())
                                          .filter(skillName => skillName!=="");
        if(skillNames.length>0){
          this.addSkills(skillNames);          
          return;
        }
        this.toastr.warning("You entered an incorrect list of skills!", 'Warning!'); 
      }
    });
  }

  addSkills(skillNames: string[]): void {
    let skills : Skill[] = [];
    skillNames.forEach(name => skills.push({skillId: '', name}));
     this.skillService.addWorkerSkills(skills)
     .subscribe(data => {
      this.getWorkerSkills();
       const message = `Skill has been added successfully`;
       this.toastr.success(message, 'Success!');
    },
    errMessage => { this.toastr.warning(errMessage, 'Warning!'); }
    );  
  }

  deleteSkill(skill: Skill): void {
    const confirmDialog = this.dialog.open(ConfirmComponent, {
      data: {
        title: 'Confirm you want to delete the skill',
        message: 'Are you sure, you want to delete the skill ' + skill.name +  '?'
      }
    });
    confirmDialog.afterClosed().subscribe(result => {
      if (result === true) {
          this.skillService.deleteWorkerSkill(skill.skillId)
          .subscribe(data => {
            this.getWorkerSkills();
            this.toastr.success('Skill has been deleted successfully', 'Success!');
          },
          (error) => { this.toastr.error('The skills were not deleted!', 'Error!')} 
          );
        }
    });
  }
}
