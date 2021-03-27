import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

import { Project } from '../../common/model/project';
import { ProjectService } from '../../common/services/project.service';

@Component({
  selector: 'app-project-add',
  templateUrl: './project-add.component.html',
  styleUrls: ['./project-add.component.css']
})
export class ProjectAddComponent{

  project: Project = new Project();

  constructor(private router: Router, private projectService: ProjectService) { }

  create(): void {
    this.projectService.save(this.project)
      .subscribe(data => {
        alert("Project has been created successfully");
      },
      er => {
        console.log(er);
      },
      () => {
        this.router.navigateByUrl(`projects`);
      });
  };

}

