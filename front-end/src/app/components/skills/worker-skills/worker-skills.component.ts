import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Skill } from '../../../common/model/skill';
import { SkillService } from '../../../common/services/skill.service';
import { ToastrService } from 'ngx-toastr';

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

  constructor(private skillService: SkillService, private toastr: ToastrService) { }

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

  addSkills(): void {
    this.skillService.addWorkerSkill({skillId: '', name: 'C#', enabled: true})
    .subscribe(data => {
      const message = `Skill has been added successfully`;
      //this.approvedSkills.push({id: '', name: value.trim(), enabled: true});
      //this.approvedSkills = this.approvedSkills.sort(this.sortByEnabledAcs);  
      this.toastr.success(message, 'Success!');
    },
    errMessage => { this.toastr.warning(errMessage, 'Warning!'); }
    );  }

  removeSkill(skill: Skill): void {
    // todo
  }
}
