import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Project } from '../common/model/project';
import { ProjectService } from '../common/services/project.service';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})

export class ProjectComponent implements OnInit {

  projects: Project[] = [];
  displayedColumns: string[] = ['name', 'description', 'projectStatus', 'creationDate'];

  constructor(private projectService: ProjectService, private router: Router) {
  }

  ngOnInit(): void {
    this.getProjects();
  }

  private getProjects() {
    this.projectService.getProjects().subscribe(
      (response: Project[]) => {
        this.projects = response;
      },
      (error) => {
        console.log("error", error);
      },
      () => {
        console.log("complete");
      }
    )
  }

  createProject() {
    this.router.navigateByUrl(`addProjects`);
  }

}
