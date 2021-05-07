import { Component, OnInit, ViewChild } from '@angular/core';
import {Router} from '@angular/router';
import { ProjectStatus } from '../../../common/model/project-status';
import { Project } from '../../../common/model/project';
import { ProjectService } from '../../../common/services/project.service';
import { ToastrService } from 'ngx-toastr';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { TokenHelper } from 'src/app/common/helper/token.helper';
import { Skill } from 'src/app/common/model/skill';
import { SkillService } from 'src/app/common/services/skill.service';
import { MatSelect } from '@angular/material/select';

@Component({
  selector: 'app-project-add',
  templateUrl: './project-add.component.html',
  styleUrls: ['./project-add.component.css']
})
export class ProjectAddComponent implements OnInit {
  projectForm: FormGroup;

  skills: Skill[] = [];

  projectStatuses: ProjectStatus[] = [
    { value: 'LOOKING_FOR_WORKER', viewValue: 'Looking for worker' },
    { value: 'DEVELOPING', viewValue: 'Developing' },
    { value: 'TESTING', viewValue: 'Testing' },
    { value: 'CLOSED', viewValue: 'Closed' }
  ];
  selectedProjectStatus: ProjectStatus = this.projectStatuses[0];

  project: Project = new Project();

  @ViewChild('skillsSelect') skillsSelect: MatSelect;

  constructor(private router: Router, private projectService: ProjectService,
              private toastr: ToastrService, private tokenHelper: TokenHelper,
              private skillService: SkillService) {
    this.project.projectStatus = this.selectedProjectStatus.value;
    this.initForm();
   }
   ngOnInit(): void {
     console.log('add');
     this.loadAllSkills();
     console.log('Load skills', this.loadAllSkills);
     // this.project.skills = this.mapSkills();
   }

   private initForm(
     name: string = '',
     description: string = '',
     skillStrings: string[] = [],
     projectStatus: string = this.selectedProjectStatus.viewValue,

   ): void {
     this.projectForm = new FormGroup({
       name: new FormControl(
         name,
         [Validators.required, Validators.minLength(2), Validators.maxLength(50)]),
         description: new FormControl(
           description,
           [Validators.required, Validators.minLength(20), Validators.maxLength(2000)]),
           projectStatus: new FormControl(),
           skills: new FormControl(skillStrings),

     });
   }

  create(): void {
    this.project.ownerId = this.tokenHelper.getIdFromToken();
    this.projectService.save(this.project)
      .subscribe(data => {
        this.toastr.success('Project has been created successfully', 'Success!');
      },
      (error) => {
        this.initForm(
          this.project.name,
          this.project.description
        );
        this.toastr.error('Project is not unique', 'Error');
      },
      () => {
        this.router.navigateByUrl('projects');
      });
  }

  private loadAllSkills(): void {
    this.skillService.getSkills()
      .subscribe(
        response => {
          this.skills = response;
          // let selectedSkills = this.project.skills.map(s => s.name);
          // this.projectForm.controls.skills.setValue(selectedSkills);
        },
        error => this.toastr.error(error, 'error')
      );

  }

  public setSelectedSkills(data: any): void {
    console.log(data);
    if (data.selected) {
      this.project.skills.push(data.value);
    } else {
      if (this.project.skills.find(x => x === data.value)) {
        this.project.skills.splice(
          this.project.skills.findIndex(x => x === data.value), 1);
      }
    }
  }

  private mapSkills(): Skill[] {
    const newSkills: Skill[] = [];
    this.project.skills.forEach(selectedSkill => {
      const skill = this.skills.find(s => s.name === selectedSkill.name);
      if (skill) {
        newSkills.push(skill);
      }
    });
    return newSkills;
  }


  public getProjectStatus(value: string): void {
    this.projectStatuses.forEach(ps => {
      if (ps.value === value) {
        this.project.projectStatus = ps.value;
      }
    });
  }
}
