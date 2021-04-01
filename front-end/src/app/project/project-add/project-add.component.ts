import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import { ProjectStatus } from '../../common/model/project-status';
import { Project } from '../../common/model/project';
import { ProjectService } from '../../common/services/project.service';

@Component({
  selector: 'app-project-add',
  templateUrl: './project-add.component.html',
  styleUrls: ['./project-add.component.css']
})
export class ProjectAddComponent{

  projectStatuses: ProjectStatus[] = [
    { value: 'LOOKING_FOR_WORKER', viewValue: 'Looking for worker' },
    { value: 'DEVELOPING', viewValue: 'Developing' },
    { value: 'TESTING', viewValue: 'Testing' },
    { value: 'CLOSED', viewValue: 'Closed' }
  ];
  selectedProjectStatus: ProjectStatus = this.projectStatuses[0];

  project: Project = new Project();

  constructor(private router: Router, private projectService: ProjectService) {
    this.project.projectStatus = this.selectedProjectStatus.value;
   }

  create(): void {
    this.projectService.save(this.project)
      .subscribe(data => {
        alert("Project has been created successfully");
      },
      er => {
        console.log(er);
      },
      () => {
        this.router.navigateByUrl(`projects/1/desc`);
      });
  };

  public getProjectStatus(value: string): void {
    this.projectStatuses.forEach(ps => {
      if (ps.value === value) {
        this.project.projectStatus = ps.value;
      }
    });
  }

}

